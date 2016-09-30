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
            controller: 'txnSaleListCtrl',
            templateUrl:'views/pages/generalList.html',
            access: "user",
            /*
            controller: 'MainCtrl',
            templateUrl:'views/dashboard/home.html',
            */
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
        .state('dashboard.createSupplier',{
            url:'/createSupplier',
            controller: 'supplierCtrl',
            templateUrl:'views/pages/supplier.html',
            access: "user"
        })
        .state('dashboard.listSupplier',{
            url:'/listSupplier',
            controller: 'supplierListCtrl',
            templateUrl:'views/pages/supplierList.html',
            access: "user"
        })

        .state('dashboard.createProduct',{
            url:'/createProduct',
            controller: 'productCtrl',
            templateUrl:'views/pages/product.html',
            access: "user"
        })
        .state('dashboard.listProduct',{
            url:'/listProduct',
            controller: 'productListCtrl',
            templateUrl:'views/pages/productList.html',
            access: "user"
        })
        .state('dashboard.uploadFile',{
            url:'/uploadFile',
            controller: 'UploadController',
            templateUrl:'views/pages/FileUpload.html',
            access: "user"
        })
        .state('dashboard.viewBoqDetail',{
            url:'/viewBoqDetail',
            controller: 'boqDetailListCtrl',
            templateUrl:'views/pages/boqWhole.html',
            access: "user"
        })
        .state('dashboard.boqList',{
            url:'/boqList',
            controller: 'boqListCtrl',
            templateUrl:'views/pages/boqHeaderList.html',
            access: "user"
        })
        .state('dashboard.purchaseOrderList',{
            url:'/purchaseOrderList',
            controller: 'purchaseOrderListCtrl',
            templateUrl:'views/pages/purchaseOrderList.html',
            access: "user"
        })
        .state('dashboard.purchaseOrderDetail',{
            url:'/purchaseOrderDetail',
            controller: 'purchaseOrderDetailCtrl',
            templateUrl:'views/pages/purchaseOrderDetail.html',
            access: "user"
        })
        .state('dashboard.deliveryNoteList',{
            url:'/deliveryNoteList',
            controller: 'deliveryNoteListCtrl',
            templateUrl:'views/pages/deliveryNoteList.html',
            access: "user"
        })
        .state('dashboard.deliveryNote',{
            url:'/deliveryNote',
            controller: 'deliveryNoteCtrl',
            templateUrl:'views/pages/deliveryNote.html',
            access: "user"
        })
        .state('dashboard.createSaleTransaction',{
            url:'/createSaleTransaction',
            controller: 'txnSaleCtrl',
            templateUrl:'views/pages/txnSale.html',
            access: "user",
            params: {
                txnType:null
            }
        })
        .state('dashboard.listSaleTransaction',{
            url:'/listSaleTransaction',
            controller: 'txnSaleListCtrl',
            templateUrl:'views/pages/generalList.html',
            access: "user"
        })
        .state('dashboard.addUser',{
            url:'/addUser',
            controller: 'userCtrl',
            templateUrl:'views/pages/user.html',
            access: "user"
        })
        .state('dashboard.listUser',{
            url:'/listUser',
            controller: 'userListCtrl',
            templateUrl:'views/pages/userList.html',
            access: "user"
        })
        .state('dashboard.addRole',{
            url:'/addRole',
            controller: 'roleCtrl',
            templateUrl:'views/pages/role.html',
            access: "user"
        })
        .state('dashboard.listRole',{
            url:'/listRole',
            controller: 'roleListCtrl',
            templateUrl:'views/pages/roleList.html',
            access: "user"
        })
        .state('dashboard.login',{
            url:'/login',
            controller: 'loginController',
            templateUrl:'views/pages/login.html'
        })
        .state('dashboard.logout',{
            url:'/logout',
            controller: 'logoutController'
        })

        .state('dashboard.pdfViewer',{
            url:'/pdfViewer',
            controller: 'pdfViewerCtrl',
            templateUrl:'views/pages/pdfViewer.html',
            access: "user"
        })

        .state('dashboard.listInvoice',{
            url:'/listInvoice',
            controller: 'invoiceListCtrl',
            templateUrl:'views/pages/invoiceList.html',
            access: "user"
        })

        .state('dashboard.debtorPayment',{
            url:'/debtorPayment',
            controller: 'debtorPaymentCtrl',
            templateUrl:'views/pages/debtorPayment.html',
            access: "user"
        })
        .state('dashboard.addFloat',{
            url:'/addFloat',
            controller: 'addFloatCtrl',
            templateUrl:'views/pages/addFloat.html',
            access: "user"
        })
        .state('dashboard.listCurrentCashSession',{
            url:'/listCurrentCashSession',
            controller: 'cashSessionListCtrl',
            templateUrl:'views/pages/cashSessionList.html',
            access: "user"
        })

        .state('dashboard.reconcileSession',{
            url:'/reconcileSession',
            controller: 'reconcileSessionCtrl',
            templateUrl:'views/pages/reconcileSession.html',
            access: "user"
        })

        .state('dashboard.blank',{
            templateUrl:'views/pages/blank.html',
            url:'/blank'
        })
        .state('login',{
            templateUrl:'views/pages/login.html',
            url:'/login'
        })
        .state('dashboard.firstPage',{
            url:'/firstPage',
            templateUrl:'views/pages/firstPage.html'
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
} ]).run(['$rootScope','AccessChecker2',function ($rootScope, AccessChecker2) {

    $rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
        $rootScope.previouseState = from.name;
        AccessChecker2.checkAcess(to.name);
    });

}]);

