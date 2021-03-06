/**
 * @overview ccm component for parkhaus
 * @author Manfred Kaul <manfred.kaul@h-brs.de> 2017, 2018, 2019, 2020, 2021
 * @author modified by Niclas Bartsch 2021
 * @license The MIT License (MIT)
 */

( function () {

  var component  = {

    name: 'parkhaus-charts',
    version: [1,0,0],

    ccm: 'https://kaul.inf.h-brs.de/ccmjs/ccm/versions/ccm-26.3.1.min.js',

    helper: [ "ccm.load", "https://kaul.inf.h-brs.de/ccmjs/akless-components/modules/versions/helper-7.2.0.min.mjs" ],

    config: {
      // server_url: "http://localhost:8080/DemoServlet7",
      html: {
        main: {
          inner: [
            { class: 'button_container', inner: [ { tag: 'span', class: 'extra_buttons' } ] },
            { class: 'button_container', inner: [ { tag: 'span', class: 'extra_charts' } ] },
            { tag: 'div', class: 'errors', style: 'display: none;' }
          ]
        },
        extra_button_div: { inner: [
            { tag: 'button', class: '%extra_class%', inner: '%extra_inner%', title: '%extra_popup_title%' },
            { tag: 'span', class: '%extra_class%' }
          ] },
        extra_chart_div: { inner: [
            { tag: 'button', class: '%extra_class%', inner: '%extra_inner%', title: '%extra_popup_title%' },
            { tag: 'input', class: '%extra_class%', type: 'checkbox' },
            { tag: 'div', class: '%extra_class%' }
          ] }
      },

      format: "json",

      chart: [ "ccm.component", "https://kaul.inf.h-brs.de/ccmjs/mkaul-components/plotly/versions/ccm.plotly-1.1.2.js" ],

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
          oninput,
        } ) );

        const error_div = main_elem.querySelector('.errors');

        // render content to website
        // $.setContent( self.element, main_elem );
        self.element.textContent = '';
        self.element.appendChild( main_elem );


        // event handler for input elements
        async function oninput( e ) {
          const newValue = parseInt(this.value) || 0;
          csv_post_event('change_' + this.classList.toString(), self[this.classList.toString()], this.value);
          self[ this.classList.toString() ] = newValue;
          switch( this.classList.toString() ){
            case "max":
              await garage.add_image( newValue - garage.max );
              garage.max = newValue;
              garage.rerender();
              break;
            case "open_from": case "open_to":
              // ToDo
              break;
            default:
              debugger;
          }
        }

        // insert extra buttons
        const extra_div = main_elem.querySelector( '.extra_buttons' );
        if ( self.extra_buttons ){
          self.extra_buttons.forEach( extra_params => {
            if ( typeof extra_params === "string" ){
              const extra_string = extra_params;
              extra_params = {
                "extra_class": extra_string,
                "extra_inner": extra_string,
                "extra_popup_title": extra_string
              };
            }
            const extra_sub_div = $.html( self.html.extra_button_div, extra_params );
            extra_div.appendChild( extra_sub_div );
            const extra_button = extra_sub_div.querySelector('button');
            const extra_span = extra_sub_div.querySelector('span');
            extra_button.addEventListener('click', async function( e ){
              if ( extra_button.classList.contains( 'start' ) ){
                if ( extra_button.classList.length > 1 ) extra_button.classList.remove( 'start' );
                await csv_get_request( extra_button.classList.toString().replaceAll(/\s/g,'_'), {}, extra_span );
                setTimeout( self.start, 800 );
              } else {
                await csv_get_request( extra_params.extra_class, {}, extra_span );
              }
            });
          });
        }

        // insert extra charts
        const extra_charts = main_elem.querySelector( '.extra_charts' );
        if ( self.extra_charts ){
          self.extra_charts.forEach(extra_params => {
            if ( typeof extra_params === "string" ){
              const extra_string = extra_params;
              extra_params = {
                "extra_class": extra_string,
                "extra_inner": extra_string,
                "extra_popup_title": extra_string
              };
            }
            const extra_sub_div = $.html( self.html.extra_chart_div, extra_params );
            extra_charts.appendChild( extra_sub_div );
            const extra_chart = extra_sub_div.querySelector('button');
            const checkbox = extra_sub_div.querySelector('input');
            checkbox.checked = true;
            checkbox.style.display = 'none';
            checkbox.addEventListener('click', function(e){
              if ( extra_div.style.display === 'none' ){
                extra_div.style.display = 'block';
              } else {
                extra_div.style.display = 'none';
              }
            });
            const extra_div = extra_sub_div.querySelector('div');
            extra_chart.addEventListener('click',async function( e ){
              let config, response;
              try {
                response = await csv_get_request( extra_params.extra_class, {} );
                if ( response ){
                  try {
                    config = JSON.parse( response );
                    config.root = extra_div;
                    self.chart.start( config );
                    checkbox.style.display = 'inline';
                  } catch (err2){
                    console.log( err2, " in HTTP Response: ", response );
                  }
                } else {
                  console.log( 'No HTTP-GET handler for ' + extra_params.extra_class );
                }
              } catch( err1 ){
                show_error( "GET " + extra_params.extra_class + ": " + err1.toString() + "<br>" + response )
              }
            });
          });
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
        async function csv_post_event( event, ...values ){
          if ( self.server_url ){
            const request = [ event, ...values ].join(",");
            const response = await fetch( new Request( self.server_url ), {
              method: 'POST',
              mode: 'cors',
              cache: 'no-store',
              body: request,
              headers:{
                'Content-Type': 'text/plain'
              }
            }).catch( err => { console.error( err ); return { text: _=> err } } );
            const response_string = (await response.text()).trim();
            const result = command_interpreter( response_string );
            console.log( request, " => ", result );
            // const lastHash = self.ccm.allHashes[ values[ 4 ] ];
            // if ( self.debug && event === 'enter' && lastHash && lastHash.nr === values[ 0 ] && lastHash.begin !== values[ 1 ] ) debugger;
            // self.ccm.allHashes[ values[ 4 ] ] = { nr: values[ 0 ], begin: values[ 1 ] };
            if ( result === 0 || result === "0" ) debugger;
            return result;
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
