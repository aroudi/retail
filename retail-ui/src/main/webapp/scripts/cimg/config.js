cimgApp.config(['$stateProvider','$urlRouterProvider','$ocLazyLoadProvider',function ($stateProvider,$urlRouterProvider,$ocLazyLoadProvider) {

    $ocLazyLoadProvider.config({
        debug:false,
        events:true
    });

    $urlRouterProvider.otherwise('/dashboard/home');

    $stateProvider
        .state('dashboard', {
            url:'/dashboard',
            templateUrl: 'views/dashboard/main.html',
            resolve: {
                loadMyDirectives:function($ocLazyLoad){
                    return $ocLazyLoad.load(
                        {
                            name:'sbAdminApp',
                            files:[
                                'scripts/directives/header/header.js',
                                'scripts/directives/header/header-notification/header-notification.js',
                                'scripts/directives/sidebar/sidebar.js',
                                'scripts/directives/sidebar/sidebar-search/sidebar-search.js'
                            ]
                        }),
                        $ocLazyLoad.load(
                            {
                                name:'toggle-switch',
                                files:["bower_components/angular-toggle-switch/angular-toggle-switch.min.js",
                                    "bower_components/angular-toggle-switch/angular-toggle-switch.css"
                                ]
                            }),
                        $ocLazyLoad.load(
                            {
                                name:'ngAnimate',
                                files:['bower_components/angular-animate/angular-animate.js']
                            })
                    $ocLazyLoad.load(
                        {
                            name:'ngCookies',
                            files:['bower_components/angular-cookies/angular-cookies.js']
                        })
                    $ocLazyLoad.load(
                        {
                            name:'ngResource',
                            files:['bower_components/angular-resource/angular-resource.js']
                        })
                    $ocLazyLoad.load(
                        {
                            name:'ngSanitize',
                            files:['bower_components/angular-sanitize/angular-sanitize.js']
                        })
                    $ocLazyLoad.load(
                        {
                            name:'ngTouch',
                            files:['bower_components/angular-touch/angular-touch.js']
                        })
                }
            }
        })
        .state('dashboard.home',{
            url:'/home',
            controller: 'MainCtrl',
            templateUrl:'views/dashboard/home.html',
            resolve: {
                loadMyFiles:function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name:'sbAdminApp',
                        files:[
                            'scripts/controllers/main.js',
                            'scripts/directives/timeline/timeline.js',
                            'scripts/directives/notifications/notifications.js',
                            'scripts/directives/chat/chat.js',
                            'scripts/directives/dashboard/stats/stats.js'
                        ]
                    })
                }
            }
        })
        .state('dashboard.form',{
            templateUrl:'views/form.html',
            url:'/form'
        })
        .state('dashboard.createCustomer',{
            url:'/createCustomer',
            controller: 'customerCtrl',
            templateUrl:'views/pages/customer.html',
            access: "user"
        })
        .state('dashboard.listCustomer',{
            url:'/listCustomer',
            controller: 'customerListCtrl',
            templateUrl:'views/pages/customerList.html',
            access: "user"
        })
        .state('dashboard.login',{
            url:'/login',
            controller: 'loginController',
            templateUrl:'views/pages/login.html'
        })
        .state('dashboard.blank',{
            templateUrl:'views/pages/blank.html',
            url:'/blank'
        })
        .state('login',{
            templateUrl:'views/pages/login.html',
            url:'/login'
        })
    /**
     }]).run(function($rootScope, AccessChecker) {
    $rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
        $rootScope.previouseState = from.name;
        AccessChecker.checkAcess(to.access);
        alert('3333');
    });
});
     **/
} ]).run(['$rootScope','AccessChecker',function ($rootScope, AccessChecker, UserService, $http, $location) {

    $rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
        $rootScope.previouseState = from.name;
        //AccessChecker.checkAcess(to.access);
    });

}]);

