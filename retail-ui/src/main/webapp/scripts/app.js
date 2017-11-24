'use strict';
/**
 * @ngdoc overview
 * @name sbAdminApp
 * @description
 * # sbAdminApp
 *
 * Main module of the application.
 */
var cimgApp = angular
  .module('sbAdminApp', [
    'angular-cron-jobs',
    'oc.lazyLoad',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'ngAnimate',
    'ngTouch',
    'ui.grid',
    'ui.grid.expandable',
    'ui.grid.pagination',
    'ui.grid.selection',
    'ui.grid.pinning',
    'treeControl',
    'angularFileUpload',
    'loadDisplay',
    'ngDialog',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.grid.resizeColumns',
    'ui.grid.moveColumns',
    'ngMessages',
    'ngStorage',
    'ngSanitize',
    'pdfjsViewer',
    'chart.js',
    'ui.grid.exporter',
    'ui.select'
  ]);

//SIT
/*
var config_data = {
    'SERVER' : 'pos.jomon.com.au',
    'PORT'   : '8080',
    'WEBAPP' :'retail-web-services'
}
*/
//SIT-9090
/*
var config_data = {
    'SERVER' : 'pos.jomon.com.au',
    'PORT'   : '9090',
    'WEBAPP' :'retail-web-services'
}
*/
//DEV
var config_data = {
    'SERVER' : 'localhost',
    'PORT'   : '8082',
    'WEBAPP' :'retail-web-services'
}
var service_uri = {
    'CUSTOMER_ALL_URI' : 'customer/all',
    'CUSTOMER_ADD_URI' : 'customer/add',
    'CUSTOMER_GET_URI' : 'customer/get',
    'CUSTOMERGRADE_ALL_URI' : 'customer/allGrades',
    'CUSTOMER_GET_ACCOUNT_DEBT_URI' : 'customer/getAccountDebt/',
    'CUSTOMER_STATEMENTS_URI' : 'customer/generateCustomerStatements',
    'CUSTOMER_GET_ACCOUNT_PAYMENT_URI' : 'customer/getAccountPayment/',
    'CUSTOMER_ALL_ACCOUNT_DEBT_URI' : 'customer/getAccountDebt/all',
    'CUSTOMER_ALL_INVOICE_URI' : 'transaction/invoice/customer/',
    'CUSTOMER_ALL_SALEORDER_URI' : 'transaction/saleOrder/customer/',
    'CUSTOMER_ALL_BOQ_URI' : 'customer/getBillOfQuantities/',
    'CUSTOMER_CONTACT_LIST_URI' : 'customer/getContactList/',
    'ALL_CATEGORY_OFTYPE_URI' : 'categories/',
    'SUPPLIER_ALL_URI' : 'supplier/all',
    'SUPPLIER_ADD_URI' : 'supplier/add',
    'SUPPLIER_GET_URI' : 'supplier/get',
    'SUPPLIER_GET_PO_LIST_URI' : 'purchaseOrder/getSupplierPurchaseOrders/',
    'SUPPLIER_GET_DN_LIST_URI' : 'deliveryNote/getSupplierDeliveryNotes/',
    'SUPPLIER_GET_PRODUCT_LIST_URI' : 'supplier/productList/',
    'SUPPLIER_GET_PRODUCT_LIST_WITH_PRICE_URI' : 'supplier/productListWithPrice/',
    'UNOM_ALL_URI' : 'unitOfMeasure/all',
    'TAXRULE_ALL_URI' : 'taxRule/all',
    'TAXLEGVARIANCE_ALL_URI' : 'taxRule/taxLegVariance/allActive',
    'PRODUCT_ALL_URI' : 'product/all',
    'PRODUCT_ALL_PAGING_URI' : 'product/all/paging',
    'PRODUCT_ADD_URI' : 'product/add',
    'PRODUCT_GET_URI' : 'product/get',
    'PRODUCT_UPLOAD_CSV_URI' : 'product/uploadCsv',
    'PRODUCT_GET_PO_LIST_URI' : 'purchaseOrder/getProductPurchaseOrders/',
    'PRODUCT_SEARCH_PAGING_URI' : 'product/searchProductPaging',
    'PRODUCT_CLASS_ALL_URI' : 'product/allProductClass',
    'PRODUCT_RESERVATION_INFO_URI' : 'product/getProductReservationInfo/',
    'PRODUCT_CHECK_EXISTENCE_URI' : 'product/checkIfProductExists/',
    'UPLOAD_BOQ_URI'  : 'billOfQuantity/upload',
    'BOQDETAIL_GET_PER_BOQID_URI' : 'billOfQuantity/detail/getPerBoqId/',
    'BOQ_GET_ALL_URI' : 'billOfQuantity/header/getAll',
    'BOQ_GET_URI' : 'billOfQuantity/header/get/',
    'BOQ_SEARCH_PAGING_URI' : 'billOfQuantity/searchBoqPaging',
    'PROJECT_GET_ALL_URI' : 'billOfQuantity/getAllProjects',
    'UPDATE_BOQ_STOCK_URI'  : 'billOfQuantity/updateStockQty',
    'BOQ_EXPORT_PICKING_SLIP_PDF' : 'billOfQuantity/pickingSlip/exportPdf/',
    'BOQ_DELETE_LIST_URI' : 'billOfQuantity/deleteBoqList',
    'MEDIA_TYPE_ALL_URI' : 'paymentMedia/getAllMediaTypes',
    'MEDIA_TYPE_GET_BYNAME_URI' : 'paymentMedia/getMediaTypeByName/',
    'PAYMENT_MEDIA_OF_TYPE_URI' : 'paymentMedia/getOfMediatype/',
    'PAYMENT_MEDIA_ALL_URI' : 'paymentMedia/getAll',
    'PRODUCT_SALE_ITEM_SEARCH_URI' : 'product/allProductSaleItem/',
    'PRODUCT_SALE_ITEM_GET_BY_SKU_URI' : 'product/saleItem/getBySku/',
    'PRODUCT_SALE_ITEM_GET_BY_PROD_ID_URI' :'product/saleItem/getByProdId/',
    'PRODUCT_AUDIT_TRAIL_URI' :'product/getProductAuditTrail/',
    'TXN_ALL_URI' : 'transaction/all',
    'TXN_ADD_URI' : 'transaction/add',
    'TXN_ADD_ACC_PAYMENT' : 'transaction/txnAccPayment/add',
    'TXN_INVOICE_URI' : 'transaction/invoice',
    'TXN_ADD_PAYMENT_URI' : 'transaction/addPayment',
    'TXN_GET_URI' : 'transaction/get/',
    'TXN_REFUND_URI' : 'transaction/refund',
    'TXN_SEARCH_URI' : 'transaction/searchTxnHeader',
    'TXN_SEARCH_PAGING_URI' : 'transaction/searchTxnHeaderPaging',
    'TXN_EXPORT_PDF' : 'transaction/exportPdf/',
    'DELIVERY_DOCKET_REPRINT_URI' : 'transaction/reprintDeliveryDocket/',
    'TXNS_OF_PRODUCT' : 'product/getTxnOfProduct/',
    'INVOICE_OF_PRODUCT' : 'product/getInvoiceOfProduct/',
    'QUOTE_DELETE_URI' : 'transaction/deleteQuote/',
    'CREATE_PO_FROM_BOQ_URI' : 'billOfQuantity/generatePurchaseOrder',
    'POH_GET_ALL_URI' : 'purchaseOrder/header/all',
    'POH_GET_URI' : 'purchaseOrder/header/getWhole/',
    'POH_EXPORT_PDF' : 'purchaseOrder/exportPdf/',
    'GET_PURCHASE_ITEMS_PER_SUPPLIER_URI' : 'purchaseOrder/detail/getPurchaseItems/',
    'GET_PURCHASE_ITEM_PER_SUPPLIER_CATALOG_URI' : 'purchaseOrder/detail/getPurchaseItem/',
    'GET_PURCHASE_ITEM_PER_ID_URI' : 'purchaseOrder/detail/getPurchaseItemPerId/',
    'POH_SAVE_URI' : 'purchaseOrder/add',
    'POH_SEARCH_URI' : 'purchaseOrder/header/search',
    'POH_SEARCH_PAGING_URI' : 'purchaseOrder/header/searchPaging',
    'POH_UPDATE_LINKED_BOQS_URI' : 'purchaseOrder/updateLinkedBoqs',
    'POH_GET_ALL_CONFIRMED_PER_SUPPLIER_URI' : 'purchaseOrder/header/search/orguIdSupIdStatusId/',
    'POH_GET_ALL_OUTSTANDING_AND_CONFIRMED_PER_SUPPLIER_URI' : 'purchaseOrder/header/getAllOutstandingAndConfirmed/',
    'POH_GET_ALL_POH_NOT_FULLY_RECEIVED_URI':'purchaseOrder/header/getAllNotFullyReceived',
    'POH_DELETE_URI' : 'purchaseOrder/header/delete/',
    'DEL_NOTE_SAVE_URI' : 'deliveryNote/add',
    'DEL_NOTE_SEARCH_URI' : 'deliveryNote/header/search',
    'DEL_NOTE_SEARCH_PAGING_URI' : 'deliveryNote/header/searchPaging',
    'DEL_NOTE_GET_ALL_URI' : 'deliveryNote/header/all',
    'DEL_NOTE_GET_URI' : 'deliveryNote/header/getWhole/',
    'USER_ALL_URI' : 'user/allValidUser',
    'USER_ADD_URI' : 'user/addUser',
    'USER_GET_URI' : 'user/getUser/',
    'USER_ACTIVATE_URI' : 'user/activateUser/',
    'USER_DEACTIVATE_URI' : 'user/deActivateUser/',
    'USER_DELETE_URI' : 'user/deleteUser/',
    'ACCESSPOINT_ALL_URI' : 'user/allAccessPoints',
    'ROLE_ALL_URI' : 'user/allValidRole',
    'ROLE_ADD_URI' : 'user/addRole',
    'ROLE_GET_URI' : 'user/getRole/',
    'ROLE_DELETE_URI' : 'user/deleteRole/',
    'LOGIN_URI' : 'user/login',
    'LOGOUT_URI' : 'user/logout',
    'CHANGE_PASSWORD_URI' : 'user/changePassword',
    'INVOICE_ALL_URI' : 'transaction/invoice/all',
    'INVOICE_GET_URI' : 'transaction/getInvoice/',
    'INVOICE_EXPORT_PDF' : 'transaction/invoice/exportPdf/',
    'INVOICE_SEARCH_URI' : 'transaction/searchInvoice',
    'INVOICE_SEARCH_PAGING_URI' : 'transaction/searchInvoicePaging',
    'CASH_SESSION_ALL_CURRENT_URI' : 'cashSession/getAllCurrentSession',
    'CASH_SESSION_ADD_FLOAT_URI' : 'cashSession/addFloat',
    'CASH_SESSION_FETCH_DATA_FOR_RECONCILIATION_URI' : 'cashSession/fetchSessionTotalForReconciliation/',
    'CASH_SESSION_RECONCILE_URI' : 'cashSession/reconcileSession',
    'CASH_SESSION_END_URI' : 'cashSession/endSession',
    'CASH_SESSION_ALL_RECONCILED_URI' : 'cashSession/getAllReconciledSessions',
    'CASH_SESSION_RECONCILIATION_TO_PDF_URI' : 'cashSession/reconciliation/exportPdf/',
    'CASH_SESSION_ACCOUNTING_REPORT_URI' : 'cashSession/accountingReport',
    'CASH_SESSION_ACCOUNTING_SUMMARY_URI' : 'cashSession/accountingSummaryReport',
    'SALE_SUMMARY_REPORT_URI' : 'saleSummaryReport/saleSummary',
    'ACCOUNTING_EXPORT_INIT_FORM_URI' : 'accounting/initiateForm',
    'ACCOUNTING_EXPORT_URI' : 'accounting/exportAsTxt',
    'ACCOUNT_GET_ALL_URI' : 'accounting/getAllAccounts',
    'ACCOUNT_UPDATE_CODE_URI' : 'accounting/updateAccounts',
    'ACCOUNTING_EXPORT_ALL_URI' : 'accounting/getAllAccountingExport',
    'ACCOUNTING_EXPORT_GET_CONTENT_URI' : 'accounting/getAccountingExport/',
    'PRICING_GRADE_UPDATE_BATCH_URI' : 'categories/updatePricingGrades',
    'GET_DATA_CHANGE_INDICATOR_URI' : 'user/getDataChangeIndicator/',
    'PRODUCT_PRICE_UPDATE_BULK' : 'product/updateProductPriceInBulk/',
    'GENERATE_PO_FROM_SO_URI' : 'saleOrder/convertSoToPo',
    'POH_OF_SO_URI' : 'purchaseOrder/header/ofSaleOrder/',
    'INVOICE_OF_SO_URI' : 'saleOrder/getInvoiceOfSo/',
    'INVOICE_IMPORT_URI' : 'transaction/importInvoice'

};

var response_status = {
    'SUCCESS' : 1,
    'FAILURE' : -1
};

var type_constant = {
    'SUPPLIER_TYPE_URI' : 'categories/SUPPLIER_TYPE',
    'CUSTOMER_TYPE_URI' : 'categories/CUSTOMER_TYPE',
    'CUSTOMER_STATUS_URI' : 'categories/CUSTOMER_STATUS',
    'SUPPLIER_STATUS_URI' : 'categories/SUPPLIER_STATUS',
    'PRODUCT_STATUS_URI' : 'categories/PRODUCT_STATUS',
    'PRODUCT_TYPE_URI' : 'categories/PRODUCT_TYPE',
    'POH_CREATION_TYPE_URI' : 'categories/POH_CREATION_TYPE',
    'POL_CREATION_TYPE_MANUAL' : 'categories/POH_CREATION_TYPE/POH_CREATION_TYPE_MANUAL',
    'POL_CREATION_TYPE_AUTO' : 'categories/POH_CREATION_TYPE/POH_CREATION_TYPE_AUTO',
    'POH_STATUS_IN_PROGRESS' : 'categories/POH_STATUS/POH_STATUS_IN_PROGRESS',
    'POH_STATUS_CONFIRMED' : 'categories/POH_STATUS/POH_STATUS_CONFIRMED',
    'POH_STATUS_CANCELLED' : 'categories/POH_STATUS/POH_STATUS_CANCELLED',
    'BOQ_LINE_STATUS_VOID' : 'categories/BOQ_LINE_STATUS/BOQ_LINE_STATUS_VOID',
    'BOQ_LINE_STATUS_PENDING' : 'categories/BOQ_LINE_STATUS/BOQ_LINE_STATUS_PENDING',
    'BOQ_LINE_STATUS_PO_CREATED' : 'categories/BOQ_LINE_STATUS/BOQ_LINE_STATUS_PO_CREATED',
    'POH_STATUS_URI' : 'categories/POH_STATUS',
    'BOQ_STATUS_URI' : 'categories/BOQ_STATUS',
    'DLV_NOTE_STATUS_URI' : 'categories/DLV_NOTE_STATUS',
    'CONTACT_TYPE_URI' : 'categories/CONTACT_TYPE',
    'TXN_TYPE_QUOTE' : 'categories/TXN_TYPE/TXN_TYPE_QUOTE',
    'TXN_TYPE_SALE' : 'categories/TXN_TYPE/TXN_TYPE_SALE',
    'TXN_TYPE_REFUND' : 'categories/TXN_TYPE/TXN_TYPE_REFUND',
    'TXN_TYPE_INVOICE' : 'categories/TXN_TYPE/TXN_TYPE_INVOICE',
    'TXN_STATE_DRAFT' : 'categories/TXN_STATE/TXN_STATE_DRAFT',
    'TXN_STATE_FINAL' : 'categories/TXN_STATE/TXN_STATE_FINAL',
    'TXN_MEDIA_SALE' : 'categories/TXN_MEDIA_TYPE/TXN_MEDIA_SALE',
    'TXN_MEDIA_DEPOSIT' : 'categories/TXN_MEDIA_TYPE/TXN_MEDIA_DEPOSIT',
    'TXN_MEDIA_REFUND' : 'categories/TXN_MEDIA_TYPE/TXN_MEDIA_REFUND',
    'SESSION_EVENT_TYPE_FLOAT' : 'categories/SESSION_EVENT_TYPE/SESSION_EVENT_TYPE_FLOAT',
    'SESSION_EVENT_TYPE_PICKUP' : 'categories/SESSION_EVENT_TYPE/SESSION_EVENT_TYPE_PICKUP',
    'CASH_SESSION_STATE_ENDED' : 'categories/SESSION_STATE/SESSION_STATE_ENDED',
    'PRICING_GRADE_DEFAULT' : 'categories/getCustomerGrade/Default',
    'TAXLEGVARIANCE_GST_URI' : 'taxRule/taxLegVariance/gstCode',
    'PRICING_RULE_URI' : 'categories/PRICING_RULES',
    'TXN_STATUS_ONORDER' : 'categories/SO_STATUS/SO_STATUS_ON_ORDER',
    'TXN_STATUS_OUTSTANDING' : 'categories/SO_STATUS/SO_STATUS_OUTSTANDING',
    'SO_STATUS_URI' : 'categories/SO_STATUS',
    'COUNTRY_LIST_URI' : 'categories/TYPE_COUNTRY'

}

angular.forEach(config_data, function(key, value) {
    cimgApp.constant(value, key);
});

angular.forEach(service_uri, function(key, value) {
    cimgApp.constant(value, key);
});

angular.forEach(response_status, function(key, value) {
    cimgApp.constant(value, key);
});

angular.forEach(type_constant, function(key, value) {
    cimgApp.constant(value, key);
});


cimgApp.service('configService', function ($http, UserService, SERVER,PORT,WEBAPP) {
    var address = 'http://' + SERVER+':'+ PORT +'/' +WEBAPP + '/rest/';


    return {
        getAddress: function () {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + UserService.getUserToken();
            return address
        }
    };

});

cimgApp.service('baseDataService', function ($location, $q, $http, $window,ngDialog, configService, $sessionStorage,UserService, GET_DATA_CHANGE_INDICATOR_URI) {

    //$sessionStorage.isPageNew = true;
    //$sessionStorage.rowSelected = false;
    var pdfFile;
    return {

        setRow: function (myRow) {
            $sessionStorage.rowObject = myRow;
            //row = myRow;
        },
        getRow: function () {
            return $sessionStorage.rowObject;
        },
        setRowId: function (myRowId) {
            $sessionStorage.rowId = myRowId;
            //rowId = myRowId;
        },
        getRowId: function () {
            return $sessionStorage.rowId;
        },
        setRowSelected: function (selected) {
            $sessionStorage.rowSelected = selected;

        },
        getRowSelected: function () {
            return $sessionStorage.rowSelected;
        },
        getIsPageNew: function () {
            if ($sessionStorage.isPageNew === undefined || $sessionStorage.isPageNew === null) {
                $sessionStorage.isPageNew = true;
            }
            return $sessionStorage.isPageNew;
        },

        setIsPageNew: function (isNew) {
            $sessionStorage.isPageNew=isNew;
        },
        getBaseData: function (baseDataURI) {
            var serviceUrl = configService.getAddress() + baseDataURI;
            var promise = $http({
                url: serviceUrl ,
                method: 'GET'
            }).success(function (data) {
                return data;
            }).error(function (data) {
            });
            return promise;
        },

        getStreamDataByPost: function (rowObject, addRowUri) {
            var serviceUrl = configService.getAddress() + addRowUri;
            var promise = $http({
                url: serviceUrl ,
                method: 'POST',
                responseType: 'arraybuffer',
                data : rowObject
            }).success(function (data) {
                return data;
            }).error(function (data) {
            });
            return promise;
        },

        getStreamData: function (baseDataURI) {
            var serviceUrl = configService.getAddress() + baseDataURI;
            var promise = $http({
                url: serviceUrl ,
                method: 'GET',
                responseType: 'arraybuffer'
            }).success(function (data) {
                return data;
            }).error(function (data) {
            });
            return promise;
        },
        //return an item from the list
        populateSelectList: function (selectName, sourceData) {
            var selectedItem = sourceData[0];
            if (selectName==null )
                return selectedItem;
            var arr=sourceData;
            if (angular.isArray(arr)) {
                for (var i = 0; i < arr.length; i++) {
                    if ( arr[i].id==selectName.id  ) {
                        return arr[i];
                    }
                }
            }
            return selectedItem
        },
        populateCategoryPerDisplayName: function (displayName, sourceData) {
            var selectedItem = sourceData[0];
            if (displayName===undefined )
                return selectedItem;
            var arr=sourceData;
            if (angular.isArray(arr)) {
                for (var i = 0; i < arr.length; i++) {
                    if ( arr[i].displayName.toLowerCase().trim()===displayName.toLowerCase().trim()  ) {
                        return arr[i];
                    }
                }
            }
            return selectedItem
        },
        populateMultiSelectList: function (selectName, sourceData) {
            /*
            if (selectName==null || selectName == undefined)
                return;
            */
            var lineArr = sourceData;
            var newLines=[];
            var selectedLines = selectName;
            if (selectedLines == undefined || selectedLines == null || selectedLines.length <1) {
                if (lineArr.length>0) {
                    newLines.push(lineArr[0]);
                    return newLines;
                }
            }
            for (var i = 0; i < selectedLines.length; i++) {
                for (var j = 0; j < lineArr.length; j++) {

                    if (selectedLines[i].id==lineArr[j].id) {
                        newLines.push(lineArr[j]);
                        continue;
                    }
                }
            }
            return newLines;
        },
        arrayToCommaSeparatedFormat: function(arr) {
            var arrayContent='';
            if (arr == undefined)
                return arrayContent;
            for (var j = 0; j < arr.length-1; j++) {
                arrayContent = arrayContent + arr[j].name +', ';
            }
            if (arr.length>0)
                arrayContent = arrayContent + arr[arr.length-1].name;
            return arrayContent;
        },

        lineToCommaSeparatedFormat: function(arr) {
            var arrayContent='';
            if (arr == undefined)
                return arrayContent;
            for (var j = 0; j < arr.length-1; j++) {
                arrayContent = arrayContent + arr[j].longName +', ';
            }
            if (arr.length>0)
                arrayContent = arrayContent + arr[arr.length-1].longName;
            return arrayContent;
        },

        addRow: function (rowObject, addRowUri) {
            var serviceUrl = configService.getAddress() + addRowUri;
            var promise = $http({
                url: serviceUrl ,
                method: 'POST',
                data : rowObject,
                headers : {
                    "Content-Type" : "application/json; charset=utf-8",
                    "Accept" : "application/json"
                }
            }).success(function (data) {
                return data;
            }).error(function (data) {
            });
            return promise;
        },
        strToDate : function(strDate)
        {
            strDate = strDate.split(/[-: ]/);

            return new Date(strDate[2],strDate[1]-1,strDate[0],strDate[3],strDate[4]);
        },
        pdfViewer : function(exportUrl) {
            this.getStreamData(exportUrl).then(function(response){
                var blob = new Blob([response.data], {'type': 'application/pdf'});
                printJS({printable: window.URL.createObjectURL(blob), type: 'pdf', header: 'My cool image header'});
                //window.open(window.URL.createObjectURL(blob));

            });

            /*
            ngDialog.openConfirm({
                template:'views/pages/pdfViewer.html',
                controller:'pdfViewerCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:true,
                resolve: {url: function(){return exportUrl}, searchForm:function(){return undefined}}
            }).then (function (value){
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
            */
            //$state.go('dashboard.pdfViewer');
        },
        pdfViewerPostMethod : function(exportUrl, inputForm) {
            /*
            ngDialog.openConfirm({
                template:'views/pages/pdfViewer.html',
                controller:'pdfViewerCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:true,
                resolve: {url: function(){return exportUrl}, searchForm:function(){return inputForm}}
            }).then (function (value){
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
            */
            this.getStreamDataByPost(inputForm, exportUrl).then(function(response){
                var blob = new Blob([response.data], {'type': 'application/pdf'});
                printJS({printable: window.URL.createObjectURL(blob), type: 'pdf', header: 'My cool image header'});
                //window.open(window.URL.createObjectURL(blob));
            });

            //$state.go('dashboard.pdfViewer');
        },
        displayMessage : function (type, heading,mBody) {
            var result = false;
            var myMessage = {
                'title': heading,
                'body' : mBody,
                 'type': type,
                 'action':''
            }
            var promise = ngDialog.openConfirm({
                template:'views/pages/messageDialog.html',
                controller:'messageDialogCtrl',
                className: 'ngdialog-theme-plain custom-width',
                closeByDocument:false,
                resolve: {message: function(){return myMessage}}
            }).then (function (msg){
                    if (msg != undefined) {
                        console.log('message action:', msg.action);
                        if (msg.action =='yes') {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                    return false
                }
            );
            /*
            ngDialog.open({
                template: '<h1>'+heading+'</h1><h1> <p>' + message + '</p>',
                closeByDocument: false,
                plain: true
            });
            */
            return promise;
        },
        getArrIndexOf: function (arr,item) {
        if (arr == undefined || item== undefined)
            return -1;
        for (var j = 0; j < arr.length; j++) {
            if (arr[j].id == item.id) {
                return j;
            }
        }
        return -1;
    },
    setPdfContent: function (pdfContent) {
        pdfFile = pdfContent;
    },
    getPdfContent: function () {
        return pdfFile;
    }

}

});

cimgApp.service('purchaseOrderService', function () {
    /*userType must be user,admin or reader*/
    var sourceOfData = 'Manual';
    var generatedResultFromBoqList
    return {
        getSourceOfData: function () {
            return sourceOfData;
        },
        setSrouceOfData: function (source) {
            sourceOfData = source;
        },
        getGeneratedResultFromBoqList: function () {
            return generatedResultFromBoqList;
        },
        setGeneratedResultFromBoqList: function (result) {
            generatedResultFromBoqList = result;
        }
    };
});

cimgApp.service('multiPageService', function ($location,$http,$sessionStorage, $state, baseDataService) {
    return {
        addPage: function(pageId, pageType, pageNo, formData) {
            var pageList = $sessionStorage.pageList;
            if (pageList === undefined || pageList===null) {
                pageList = [];
            }
            var pageData;
            //delete existing form list
            if (pageId != -1) {
                this.deletePage(pageId);
            }
            pageData = {
                'id' : this.getPageSequence(),
                'pageType' : pageType,
                'dateCreated' : new Date(),
                'pageNo' : pageNo,
                'formData' : formData
            }
            pageList.push(pageData);
            $sessionStorage.pageList = pageList;
            return pageData;
        },
        getPageList: function() {
            return $sessionStorage.pageList;
        },
        getPageSequence : function() {
            var sequence = $sessionStorage.sequence;
            if (sequence === undefined || sequence === null) {
                sequence = 0;
            } else {
                sequence = sequence + 1;
            }
            $sessionStorage.sequence = sequence;
            return sequence;
        },
        deletePage : function(pageId) {
            //var pageList = $sessionStorage.pageList;
            var pageList = $sessionStorage.pageList;
            if (pageList === undefined || pageList===null ||pageList.length < 1) {
                return;
            }
            for (var i = 0; i < pageList.length; i++) {
                if (pageList[i].id === pageId) {
                    pageList.splice(i, 1);
                    $sessionStorage.pageList = pageList;
                    return;
                }
            }
        },
        removePageList : function(){
            $sessionStorage.pageList = [];
        }
    }
});


cimgApp.service('UserService', function ($location,$http,$sessionStorage, ngDialog) {
    var message;
    return {
        setUser: function (user) {
            $sessionStorage.user = user;
            //$window.$sessionStorage.addItem('user', user);
        },
        removeUser: function() {
            delete $sessionStorage.user;
            delete $sessionStorage.userAccess;
            delete $sessionStorage.userToken;
        },
        doLoggin: function (params, url) {
            var promise = $http({
                url: url,
                method: 'GET',
                params: params
            }).success(function (data) {
                return data;
            }).error(function (data) {
            });
            return promise;
        },
        getUser: function(){
            return $sessionStorage.user;
            //return $window.$sessionStorage.getItem('user');
        },
        getMessage: function () {
            return message;
        },
        setMessage: function (msg) {
            message = msg;
        },
        getUserAccess: function () {
            //return $window.$sessionStorage.getItem('userAccess');
            return $sessionStorage.userAccess;
        },
        setUserAccess: function (userAccess) {
            //$window.$sessionStorage.addItem('userAccess', userAccess);
            $sessionStorage.userAccess = userAccess;
        },
        assigneUserAccess: function (accessToken) {
            var userAccessList = $sessionStorage.userAccess;
            if (userAccessList != undefined) {
                userAccessList.push(accessToken);
                $sessionStorage.userAccess = userAccessList;
                //promote user
                var user = $sessionStorage.user;
                if (user != undefined) {
                    user.promoted = true;
                    $sessionStorage.user = user;
                }
            }
        },
        revokeUserAccess: function (accessToken) {
            var userAccessList = $sessionStorage.userAccess;
            if (userAccessList !== undefined) {
                for (var i=0; i<userAccessList.length; i++) {
                    if (userAccessList[i] === accessToken) {
                        userAccessList.splice(i, 1);
                        //check promotion flag on user
                        var user = $sessionStorage.user;
                        if (user !== undefined) {
                            user.promoted = false;
                            $sessionStorage.user = user;
                        }
                        break;
                    }
                }
                $sessionStorage.userAccess = userAccessList;
            }
        },
        checkUserHasAccess: function(accessToken) {
            var userAccessList = $sessionStorage.userAccess;
            if (userAccessList != undefined) {
                for (var i = 0; i < userAccessList.length; i++) {
                    if (userAccessList[i] === accessToken) {
                        return true;
                    }
                }
            }
            return false;
        },
        checkIfUserPromoted: function () {
            var user = $sessionStorage.user;
            if (user === undefined || user.promoted === undefined) {
                return false;
            }
            return user.promoted;

        },
        getUserToken: function () {
            //return $window.$sessionStorage.getItem('userAccess');
            return $sessionStorage.userToken;
        },

        setUserToken: function (userToken) {
            //$window.$sessionStorage.addItem('userAccess', userAccess);
            $sessionStorage.userToken = userToken;
        },
        promoteUserForAccessToken : function (accessToken) {
            var promise = ngDialog.openConfirm({
                template:'views/pages/login.html',
                controller:'userPromotionController',
                className: 'ngdialog-theme-plain custom-width',
                closeByDocument:false,
                resolve: {userAccessToken: function(){return accessToken}}
            }).then (function (result){
                    if (result != undefined) {
                        return result;
                    }
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                    return false
                }
            );
            /*
             ngDialog.open({
             template: '<h1>'+heading+'</h1><h1> <p>' + message + '</p>',
             closeByDocument: false,
             plain: true
             });
             */
            return promise;
        }
    };
});

cimgApp.service('AccessChecker2', function ($state, $rootScope, UserService, baseDataService) {
    /*userType must be user,admin or reader*/
    return {
        checkAcess: function (accessName) {
            if (accessName == 'dashboard.login' || accessName == 'dashboard.logout' || accessName == 'dashboard.firstPage') {
                return;
            }
            var userAccessList = UserService.getUserAccess();
            var user = UserService.getUser();
            if (user == undefined || user == null || userAccessList == undefined || userAccessList == null) {
                $state.go('dashboard.login');
            }
            var token = '';
            for (var i= 0; i<userAccessList.length; i++) {
               token = 'dashboard.' + userAccessList[i];
               if(token == accessName) {
                   //let proceed.
                   return;
               }
            }
            console.log('accessName = ' + accessName);
            //user dont have access. display message and return to the previouse state
            baseDataService.displayMessage("info","Warning", "Access Denied!!");
            $state.go($rootScope.previouseState);
        }
    };
});

cimgApp.service('singleFileUploadService', function ($http, $q, configService, loadDisplay) {
    return {
        uploadSimpleFileToUrl : function (file1, uploadUrl) {
            var serviceUrl = configService.getAddress() + uploadUrl;
            var fd = new FormData();
            fd.append('file', file1);
            var div = $q.defer();
            loadDisplay.addDisplay(div.promise, "Uploading file ...");
            var promise = $http.post(serviceUrl,fd,{
                trnsformRequest:angular.identity,
                headers: {'Content-Type': multipart/form-data}
            }).success(function (data) {
                div.resolve();
                return data;
            }).error(function (data) {
                div.resolve();
            });
            return promise;
        }
    }
});

cimgApp.service('fileUploadService', function ($upload, $http, $q, configService, loadDisplay) {
    return {
        uploadFileToUrl : function (files, uploadUrl) {
            var serviceUrl = configService.getAddress() + uploadUrl;
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                var div = $q.defer();
                loadDisplay.addDisplay(div.promise, "Uploading file ...");
                var promise =
                $upload.upload({
                    url: serviceUrl, // upload.php script, node.js route, or servlet url
                    method: 'POST',
                    //headers: {'Authorization': 'xxx'}, // only for html5
                    //withCredentials: true,
                    //data: {myObj: $scope.myModelObj},
                    file: file // single file or a list of files. list is only for html5
                    //fileName: 'doc.jpg' or ['1.jpg', '2.jpg', ...] // to modify the name of the file(s)
                    //fileFormDataName: myFile, // file formData name ('Content-Disposition'), server side request form name
                    // could be a list of names for multiple files (html5). Default is 'file'
                    //formDataAppender: function(formData, key, val){}  // customize how data is added to the formData.
                    // See #40#issuecomment-28612000 for sample code

                }).progress(function(evt) {
                    console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '% file :'+ evt.config.file.name);
                }).success(function(data, status, headers, config) {
                    // file is uploaded successfully
                    console.log('file ' + config.file.name + 'is uploaded successfully. Response: ' + data);
                    div.resolve();
                    return data;
                })
                    //.error(...)
                    //.then(success, error, progress); // returns a promise that does NOT have progress/abort/xhr functions
                    //.xhr(function(xhr){xhr.upload.addEventListener(...)}) // access or attach event listeners to
                    //the underlying XMLHttpRequest
                    .error(function (data) {
                        div.resolve();
                    });
                return promise;
            }
        }
    }
});

cimgApp.directive('compile', ['$compile', function ($compile) {
    return function(scope, element, attrs) {

        var ensureCompileRunsOnce = scope.$watch(
            function(scope) {
                // watch the 'compile' expression for changes
                return scope.$eval(attrs.compile);
            },
            function(value) {
                // when the 'compile' expression changes
                // assign it into the current DOM
                element.html(value);

                // compile the new DOM and link it to the current
                // scope.
                // NOTE: we only compile .childNodes so that
                // we don't get into infinite loop compiling ourselves
                $compile(element.contents())(scope);

                // Use un-watch feature to ensure compilation happens only once.
                ensureCompileRunsOnce();
            }
        );
    };
}]);

cimgApp.directive('format', ['$filter', function ($filter) {
    return {
        require: '?ngModel',
        link: function (scope, elem, attrs, ctrl) {
            if (!ctrl) return;

            ctrl.$formatters.unshift(function (a) {
                return $filter(attrs.format)(ctrl.$modelValue)
            });

            elem.bind('blur', function(event) {
                var plainNumber = elem.val().replace(/[^\d|\-+|\.+]/g, '');
                //var plainNumber = elem.val().replace(/[^0-9._-]/g, '');
                elem.val($filter(attrs.format)(plainNumber));
            });
        }
    };
}]);

cimgApp.directive('blurCurrency', ['$filter', function ($filter) {
    return {
        require: '^ngModel',
        scope: true,
        link: function link(scope, el, attrs, ngModelCtrl) {

            function formatter(value) {
                value = value ? parseFloat(value.toString().replace(/[^0-9._-]/g, '')) || 0 : 0;
                var formattedValue = $filter('currency')(value);
                el.val(formattedValue);

                ngModelCtrl.$setViewValue(value);
                scope.$apply();

                return formattedValue;
            }

            ngModelCtrl.$formatters.push(formatter);

            el.bind('focus', function () {
                //el.val('');
            });

            el.bind('blur', function () {
                formatter(el.val());
            });
        }
    };
}]);

cimgApp.filter('lineList', function() {
    return function (linesAfected) {
        var arr = linesAfected;
        var arrayContent = '';
        for (var j = 0; j < arr.length-1; j++) {
            arrayContent = arrayContent + arr[j].name +', ';
        }
        if (arr.length>0) {
            arrayContent = arrayContent + arr[arr.length - 1].name;
        }
        return arrayContent;

    };

});
cimgApp.filter('booleanFilter', function() {
    return function (status) {
        if (status == true) {
            return 'Yes'
        } else {
            return 'No'
        }
    };

});
cimgApp.filter('invoiceSource', function() {
    return function (status) {
        if (status === true) {
            return 'Doors3'
        } else {
            return 'Pos'
        }
    };
});
cimgApp.filter('accImportFilter', function() {
    return function (status) {
        if (status == true) {
            return 'Exported'
        } else {
            return 'Will be exported'
        }
    };
});
cimgApp.filter('productReservTxntypeFilter', function() {
    return function (txnType) {
        if (txnType == 'TXN_TYPE_SALE') {
            return 'Sale Order'
        } else {
            return 'Bill Of Quantity'
        }
    };
});
cimgApp.filter('configCategoryFilter', function() {
    return function (status) {
        if (status === undefined || status === null) {
            return '';
        }
        return status.displayName;
    };
});
    cimgApp.filter('fullName', function() {
        return function (lastModifiedBy) {
            if (lastModifiedBy ==undefined || lastModifiedBy ==null) {
                return '';
            }
            return lastModifiedBy.usrFirstName + ' ' + lastModifiedBy.usrSurName;
        };
    });
cimgApp.filter('poBoqLinkOrderNumberFilter', function() {
    return function (linkedPurchaseOrders) {
        if (linkedPurchaseOrders ==undefined || linkedPurchaseOrders ==null || linkedPurchaseOrders.length < 1 ) {
            return '***';
        }
        return linkedPurchaseOrders[0].pohOrderNumber;
    };
});

cimgApp.filter('trusted', ['$sce', function ($sce) {
    return function(url) {
        return $sce.trustAsResourceUrl(url);
    };
}]);

cimgApp.directive('ngAppMax', function(){
    return {
        require: 'ngModel',
        scope: {
            ngAppMax: '@',
            ngModel: '='
        },
        link: function(scope, element, attrs, ctrl){

            scope.$watch('ngAppMax', function(newVal){
                validate(scope.ngModel, newVal, ctrl);
            });

            scope.$watch('ngModel', function(val){
                validate(val, scope.ngAppMax);
            });

            function validate(thisVal, maxVal){
                if(thisVal > maxVal){
                    ctrl.$setValidity('range', false);
                } else {
                    ctrl.$setValidity('range', true);
                }
            }

        }
    }
});

cimgApp.directive('focusOn', function() {
    return function(scope, elem, attr) {
        scope.$on(attr.focusOn, function(e) {
            console.log('set focused called for ' + elem[0]);
            elem[0].focus();
        });
    };
});

cimgApp.run(function ($window) {
    var windowElement = angular.element($window);
    windowElement.on('onbeforeunload', function (event) {
        //event.returnValue = "Refresh is disabled";
        console.log('page refreshed');
        event.preventDefault();
    });

});

cimgApp.filter('propsFilter', function() {
    return function(items, props) {
        var out = [];

        if (angular.isArray(items)) {
            var keys = Object.keys(props);

            items.forEach(function(item) {
                var itemMatches = false;

                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    if (item[prop] === null || item[prop] === undefined) {
                        continue;
                    }
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
});

/*
cimgApp.config(['ChartJsProvider', function(ChartJsProvider){
    ChartJsProvider.setOptions({ chartColors : [ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
    responsive : false});
    ChartJsProvider.setOptions('line', {showLines:false});
}]);
*/
