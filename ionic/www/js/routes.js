angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
  

      .state('tabsController.criarUmaEnquete', {
    url: '/createPage',
    views: {
      'tab1': {
        templateUrl: 'templates/criarUmaEnquete.html',
        controller: 'criarUmaEnqueteCtrl'
      }
    }
  })

  .state('tabsController.responderAUmaEnquete', {
    url: '/anwerPage',
    views: {
      'tab2': {
        templateUrl: 'templates/responderAUmaEnquete.html',
        controller: 'responderAUmaEnqueteCtrl'
      }
    }
  })

  .state('tabsController', {
    url: '/page1',
    templateUrl: 'templates/tabsController.html',
    abstract:true
  })

  .state('tabsController.enqueteEmProgresso', {
    url: '/esperarEnquete',
    views: {
      'tab1': {
        templateUrl: 'templates/enqueteEmProgresso.html',
        controller: 'enqueteEmProgressoCtrl'
      }
    }
  })

  .state('tabsController.estatisticasDaEnquete', {
    url: '/estatisticasEnquete',
    views: {
      'tab1': {
        templateUrl: 'templates/estatisticasDaEnquete.html',
        controller: 'estatisticasDaEnqueteCtrl'
      }
    }
  })

  .state('tabsController.obrigado', {
    url: '/obrigado',
    views: {
      'tab2': {
        templateUrl: 'templates/obrigado.html',
        controller: 'obrigadoCtrl'
      }
    }
  })

  .state('dispositivos', {
    url: '/dispositivos',
    templateUrl: 'templates/dispositivos.html',
    controller: 'dispositivosCtrl'
  })

  .state('ligarBluetooth', {
    url: '/ligarBluetooth',
    templateUrl: 'templates/ligarBluetooth.html',
    controller: 'ligarBluetoothCtrl'
  })

$urlRouterProvider.otherwise('/page1/createPage')

  

});