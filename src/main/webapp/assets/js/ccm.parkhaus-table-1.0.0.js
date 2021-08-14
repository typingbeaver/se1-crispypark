/**
 * @overview ccm component for parkhaus
 * @author Manfred Kaul <manfred.kaul@h-brs.de> 2017, 2018, 2019, 2020, 2021
 * @author modified by Niclas Bartsch 2021
 * @license The MIT License (MIT)
 *
 */

/*
 * (VERY UGLY STRIP DOWN)
 */

( function () {

  var component  = {

    name: 'parkhaus-table',
    version: [1,0,0],

    ccm: 'https://kaul.inf.h-brs.de/ccmjs/ccm/versions/ccm-26.3.1.min.js',

    helper: [ "ccm.load", "https://kaul.inf.h-brs.de/ccmjs/akless-components/modules/versions/helper-7.2.0.min.mjs" ],

    config: {
      name: "CarHome",

      html: {
        main: {
          inner: [
            { tag: 'table', inner: [
              { tag: 'tr', inner: [ { tag: 'th', inner: 'Nr', title: 'Nr des Autos' }, { tag: 'th', inner: 'Von', title: 'Startzeit des Parkens' }, { tag: 'th', inner: 'Bis', title: 'Endzeit des Parkens' }, { tag: 'th', inner: 'Dauer', title: 'Wie lange war das Auto im Parkhaus?' }, { tag: 'th', inner: 'Ticket-Typ', title: 'TicketType' }, { tag: 'th', inner: 'Kunden-Typ', title: 'DriverType' }, { tag: 'th', inner: 'Kennzeichen', title: 'License' }, { tag: 'th', inner: 'Preis', title: 'Parkgebühren' } ] }
            ] },
            { tag: 'div', class: 'errors', style: 'display: none;' }
          ]
        },
        row: { tag: 'tr', inner: [ { tag: 'td', inner: '%nr%' }, { tag: 'td', inner: '%von%' }, { tag: 'td', inner: '%bis%' }, { tag: 'td', inner: '%dauer%' }, { tag: 'td', inner: '%ctyp%' }, { tag: 'td', inner: '%vtyp%' }, { tag: 'td', inner: '%license%' }, { tag: 'td', class: 'price', inner: '%preis%' } ] },
      },

      format: "json",

      css: [ 'ccm.load',  'https://kaul.inf.h-brs.de/ccmjs/mkaul-components/parkhaus/resources/default.css' ]
    },

    /**
     * for creating instances of this component
     * @constructor
     */
    Instance: function () {

      "use strict";

      /**
       * own reference for inner functions
       * @type {Instance}
       */
      const self = this;

      /**
       * shortcut to help functions
       * @type {Object.<string,function>}
       */
      let $;

      /**
       * is the simulation running?
       * @type {Boolean}
       */
      let simulationIsRunning = false;

      /**
       * is called once after the initialization and is then deleted
       */
      this.ready = async () => {

        // set shortcut to helper functions
        $ = Object.assign( {}, this.ccm.helper || ccm.helper, this.helper );

        // if ( ! this.ccm.allHashes ) this.ccm.allHashes = {};

      };

      /**
       * starts the instance
       */
      this.start = async () => {

        const main_elem = $.html( self.html.main, Object.assign({
          date: new Date().toLocaleString()
        }, (({ name }) => ({ name }))(self) ) );

        const table = main_elem.querySelector( 'table' );
        const error_div = main_elem.querySelector('.errors');

        const date_div = main_elem.querySelector('.date');
        if ( date_div ) setInterval(()=>{
          date_div.innerText = new Date().toLocaleString();
        },1000);


        class Car {
          constructor( spec ){
            if ( spec ){
              if ( typeof spec === 'string' ){
                [ this.nr, this.timer, this._duration, this._price, this._hash, this._color, this.space, this.client_category, this.vehicle_type, this._license ] = spec.split("/");
                [ 'nr', 'timer', '_duration', '_price', 'space' ].forEach( prop => this[prop] === '_' ? '_' : parseInt(this[prop]));
              } else if ( typeof spec === 'object' ){
                for ( const [ key, value ] of Object.entries( spec ) ){
                  if ( [ 'hash', 'price', 'license', 'duration', 'color' ].includes( key ) ){
                    this[ '_' + key ] = value;
                  } else {
                    this[ key ] = value;
                  }
                }
                this._inGarage = false;
              } else {
                this.nr = spec;
                this._inGarage = false;
              }
            } else {
              this.nr = garage.nextTotal();  // because the car has not entered the car_index
            }
          }
          clear(){
            this._inGarage = false;
            this.timer = (new Date()).getTime();
            this._duration = 0;
          }
          id(){
            return 'car_' + this.nr;
          }
          hash(){
            if ( this._hash ) return this._hash;
            return self.hash ? self.hash.md5( self.name + self.SALT + this.nr + this.timer ) : this.nr;
          }
          inGarage(){
            return this._inGarage;
          }
          get license(){
            if ( ! this._license ){
              this._license = ( self.town || 'SU' ) + '-'
                + 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.charAt(Math.floor(26*Math.random()))
                + ' '
                + randomLicense();
            }
            return this._license;
          }
          get client_category(){
            if ( self.client_categories && ! this._client_category ){
              this._client_category = randomCategory();
            }
            return this._client_category;
          }
          set client_category( cat ){
            this._client_category = cat;
          }
          get vehicle_type(){
            if ( self.vehicle_types && ! this._vehicle_type ){
              this._vehicle_type = randomVehicleType();
            }
            return this._vehicle_type;
          }
          set vehicle_type( type ){
            this._vehicle_type = type;
          }
          get space(){
            if ( ! this._space ) this.space = garage.randomSpace();
            return this._space;
          }
          set space( nr ){
            if ( nr ){
              this._space = nr;
            } else {
              this._space = garage.randomSpace();
            }
          }
          space_id(){
            return 'Space_' + this.space;
          }
          static getList( s ){
            if ( s && s.trim().startsWith('[') ){  // JSON
              return JSON.parse( s ).map( spec => new Car( spec ) );
            } else {  // CSV
              const car_strings = s ? s.split(',') : [];
              return car_strings.map( spec => new Car( spec ) );
            }
          }
          enter(){
            this._inGarage = true;
            this._duration = 0;
            this.timer = (new Date()).getTime(); // invariant: this.timer is always the begin time of parking
          }
          leave() {
            this._inGarage = false;
            this._duration = ( parseFloat( self.simulation_speed ) || 1 ) * ( (new Date()).getTime() - this.timer );
            return this._duration;
          }
          gone(){
            if ( parseInt( this._duration ) > 0 ) return true;
            return (  parseFloat( this.price() ) > 0 );
          }
          begin() { // invariant: this.timer is always the begin time of parking
            return new Date( this.beginTime() ).toLocaleString();
          }
          beginTime(){
            return simulationTime( this.timer );
          }
          end() {  // invariant: this.timer is always the begin time of parking
            const endTime = this.beginTime() + parseInt( this._duration );
            // console.log( 'begin ', this.beginTime(), ', end ', endTime, ', duration ', this._duration, ', diff ', endTime - this.beginTime() );
            // console.log( 'begin ', new Date( this.beginTime() ).toLocaleString(), ', end ', new Date( endTime ).toLocaleString(), ', duration ', time( this._duration ), ', diff ', endTime - this.beginTime() );
            return new Date( endTime ).toLocaleString();
          }
          duration() {  // ToDo 2000 msec => 01:00:02 ???
            return new Date( this._duration ).toLocaleTimeString();
          }
          price_factor(){
            let price_factor = self.price_factor;
            if ( ! price_factor ) return 1;
            if ( typeof price_factor === 'string' ){
              price_factor = price_factor.trim();
              if ( price_factor.startsWith('{') ){
                price_factor = JSON.parse( price_factor );
              } else {
                price_factor = parseFloat( price_factor );
              }
            }
            if ( typeof price_factor === 'number' ) return price_factor;
            if ( typeof price_factor === 'object' ){
              const client_price = price_factor[ this.client_category ];
              const vehicle_price = price_factor[ this.vehicle_type ];
              const client_vehicle_price = price_factor[ this.client_category + '.' + this.vehicle_type ];
              if ( typeof client_vehicle_price === 'number' ) return client_vehicle_price;
              if ( typeof client_price === 'number' ) return client_price;
              if ( typeof vehicle_price === 'number' ) return vehicle_price;
              return 1;
            }
            return 1;
          }
          price() {
            if ( this._price ) return parseFloat( this._price );
            return Math.round(this.price_factor() * this._duration / parseInt( self.simulation_speed ) );
          }
          color(){
            if ( ! this._color ){
              this._color = '#' + ('00000'+(getRandom()*(1<<24)|0).toString(16)).slice(-6);
            }
            return this._color;
          }
          toObject(){
            return {
              nr: this.nr,
              timer: this.timer,
              duration: this._duration,
              price: this.price(),
              hash: this.hash(),
              color: this.color(),
              space: this.space,
              client_category: this.client_category,
              vehicle_type: this.vehicle_type,
              license: this.license,
              begin: this.beginTime()
            };
          }
          toJSON(){
            return JSON.stringify( this.toObject(), null, 2 );
          }
          toTransferObject(){
            return self.format === 'json' ? this.toJSON() : this.toCSV();
          }
          toArray(){
            return [ this.nr, this.timer, this._duration, this.price(), this.hash(), this.color(), this.space, this.client_category, this.vehicle_type, this.license, this.beginTime() ].map( x => ( x || '_' ).toString() );
          }
          toCSV(){
            this.toArray().join(',');
          }
          tooltip(){
            return this.toArray().join(',');
          }
          image(){
            return $.html( self.car, {
              id: this.space_id(),
              nr: this.license,
              color: this.color(),
              tooltip: this.tooltip()
            });
          }
          toString(){
            return `Car(${this.nr})`; // return `new Car(${this.toJSON()})`;
          }
        }

        function simulationTime( realTime ){
          const startTime = BigInt( self.time_shift || 0 );
          const result = BigInt( self.simulation_speed || 1 ) * BigInt( realTime ) - startTime;
          const intResult = Number( result );
          const resultTime = new Date( intResult );  // for debugging only
          return intResult;
        }

        function simulationNow(){
          return simulationTime( new Date().getTime() );
        }

        // render content to website
        // $.setContent( self.element, main_elem );
        self.element.textContent = '';
        self.element.appendChild( main_elem );

        // ======= get cars from server =======
        const car_string = await csv_get_request( "carsArchive", { name: self.name } );
        const car_list = Car.getList( car_string );
        Object.assign(self, { cars: car_list });
        car_list.filter( car => car.gone() ).forEach( car => addTableRow( car ) );


        function addTableRow( car ){
          if ( table ) table.appendChild( $.html( self.html.row, {
              nr: car.nr,
              von: car.begin(),
              bis: car.end(),
              dauer: time( car._duration ),
              ctyp: car.client_category,
              vtyp: car.vehicle_type,
              ticket: car.hash(),
              license: car.license,
              color: car.color(),
              space: car.space,
              preis: ' € ' + ( car.price() / 100 ).toFixed(2) }
            )
          );
        }

        function time( nr ){
          const msec = ""+nr % 1000; nr /= 1000; nr = Math.floor( nr );
          const sec = ""+nr % 60; nr /= 60; nr = Math.floor( nr );
          const min = ""+nr % 60; nr /= 60; nr = Math.floor( nr );
          const hours = ""+nr % 24; nr /= 24; nr = Math.floor( nr );
          if ( nr === 0 && hours === '0' && min === '0' && sec === '0' ) return `0.${msec.padStart(3, '0')}`;
          if ( nr === 0 && hours === '0' ) return `${min.padStart(2, '0')}:${sec.padStart(2, '0')}`;
          if ( nr === 0 ) return `${hours.padStart(2, '0')}:${min.padStart(2, '0')}:${sec.padStart(2, '0')}`; // .${msec.padStart(3, '0')}
          const days = ""+nr;
          return `${days}.${hours.padStart(2, '0')}:${min.padStart(2, '0')}:${sec.padStart(2, '0')}`; // .${msec.padStart(3, '0')}
        }

        async function csv_get_request( command, params, extra_span ){
          if ( self.server_url ){
            const request = new Request( self.server_url
              + '?cmd=' + command
              + Object.entries( params ).map(([key, value])=>'&'+key+'='+value).join()
            );
            console.log( request );
            try {
              const response = await fetch( request, {
                method: 'GET',
                mode: 'cors',
                cache: 'no-store',
                headers:{
                  'Content-Type': 'text/html'
                }
              });
              if (!response.ok) console.error( response.statusText ) // or check for response.status
              // process body
              const response_string = (await response.text()).trim();
              return command_interpreter( response_string, extra_span );
            } catch (err) {
              console.error(err, request);
              // show_error( "<p>" + request.url + " failed.<br>" + err + "</p>" );
            }
          } else {
            // console.log( "No server_url" );
          }
        }

        function command_interpreter( response_string, extra_span ){
          if ( response_string.indexOf('HTTP Status') >= 0 ){ // error
            show_error( response_string );
          } else { // generic interpreter for server responses: Where? What? Params...
            const [ selector, command, ...content ] = response_string.split(',');
            if ( main_elem && /^[.#A-Za-z_]\w*$/.test(selector) ){
              if ( selector.startsWith('#') ){ // identifier for single element
                if ( main_elem.querySelector( selector ) ){
                  if ( command === "insertHTML"  ){
                    main_elem.querySelector( selector ).innerHTML = content;
                  } else {
                    main_elem.querySelector( selector ).innerText = content;
                  }
                } else {
                  console.log( 'selector ' + selector + ' not found.' );
                }
              } else {
                Array.from( main_elem.querySelectorAll( selector ) ).forEach( elem => {
                  if ( command === "insertHTML"  ){
                    elem.innerHTML = content;
                  } else {
                    elem.innerText = content;
                  }
                });
              }
             } else if ( extra_span ) {
              extra_span.innerHTML = response_string;
            } else {
              return response_string;
            }
          }
        }

        function show_error( message ){
          if ( ! self.debug || self.debug === "false" ) return;
          if ( typeof message === 'string' ){
            error_div.innerHTML += message;
          } else {
            error_div.appendChild( message );
          }
          if ( self.debug ) error_div.style.display = 'block';
        }

      };

    }

  };

  let b="ccm."+component.name+(component.version?"-"+component.version.join("."):"")+".js";if(window.ccm&&null===window.ccm.files[b])return window.ccm.files[b]=component;(b=window.ccm&&window.ccm.components[component.name])&&b.ccm&&(component.ccm=b.ccm);"string"===typeof component.ccm&&(component.ccm={url:component.ccm});let c=(component.ccm.url.match(/(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)/)||[""])[0];if(window.ccm&&window.ccm[c])window.ccm[c].component(component);else{var a=document.createElement("script");document.head.appendChild(a);component.ccm.integrity&&a.setAttribute("integrity",component.ccm.integrity);component.ccm.crossorigin&&a.setAttribute("crossorigin",component.ccm.crossorigin);a.onload=function(){(c="latest"?window.ccm:window.ccm[c]).component(component);document.head.removeChild(a)};a.src=component.ccm.url}
}() );
