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
    'ngMessages'
  ]);

//SIT
/*
var config_data = {
    'SERVER' : '10.0.0.17',
    'PORT'   : '8080',
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
    'ALL_CATEGORY_OFTYPE_URI' : 'categories/',
    'SUPPLIER_ALL_URI' : 'supplier/all',
    'SUPPLIER_ADD_URI' : 'supplier/add',
    'SUPPLIER_GET_URI' : 'supplier/get',
    'UNOM_ALL_URI' : 'unitOfMeasure/all',
    'TAXRULE_ALL_URI' : 'taxRule/all',
    'PRODUCT_ALL_URI' : 'product/all',
    'PRODUCT_ADD_URI' : 'product/add',
    'PRODUCT_GET_URI' : 'product/get',
    'UPLOAD_BOQ_URI'  : 'billOfQuantity/upload',
    'BOQDETAIL_GET_PER_BOQID_URI' : 'billOfQuantity/detail/getPerBoqId/',
    'BOQ_GET_ALL_URI' : 'billOfQuantity/header/getAll',
    'BOQ_GET_URI' : 'billOfQuantity/header/get/',
    'UPDATE_BOQ_STOCK_URI'  : 'billOfQuantity/updateStockQty',
    'MEDIA_TYPE_ALL_URI' : 'paymentMedia/getAllMediaTypes',
    'PAYMENT_MEDIA_OF_TYPE_URI' : 'paymentMedia/getOfMediatype/',
    'PRODUCT_SALE_ITEM_ALL_URI' : 'product/allProductSaleItem',
    'TXN_ALL_URI' : 'transaction/all',
    'TXN_ADD_URI' : 'transaction/add',
    'TXN_GET_URI' : 'transaction/get/',
    'CREATE_PO_FROM_BOQ_URI' : 'billOfQuantity/generatePurchaseOrder',
    'POH_GET_ALL_URI' : 'purchaseOrder/header/all',
    'POH_GET_URI' : 'purchaseOrder/header/getWhole/',
    'GET_PURCHASE_ITEMS_PER_SUPPLIER_URI' : 'purchaseOrder/detail/getPurchaseItems/',
    'POH_SAVE_URI' : 'purchaseOrder/add',
    'POH_UPDATE_LINKED_BOQS_URI' : 'purchaseOrder/updateLinkedBoqs',
    'POH_GET_ALL_CONFIRMED_PER_SUPPLIER_URI' : 'purchaseOrder/header/search/orguIdSupIdStatusId/',
    'DEL_NOTE_SAVE_URI' : 'deliveryNote/add',
    'DEL_NOTE_GET_ALL_URI' : 'deliveryNote/header/all',
    'DEL_NOTE_GET_URI' : 'deliveryNote/header/getWhole/'
}

var response_status = {
    'SUCCESS' : 1,
    'FAILURE' : -1
}

var type_constant = {
    'SUPPLIER_TYPE_URI' : 'categories/SUPPLIER_TYPE',
    'CUSTOMER_TYPE_URI' : 'categories/CUSTOMER_TYPE',
    'SUPPLIER_STATUS_URI' : 'categories/SUPPLIER_STATUS',
    'PRODUCT_STATUS_URI' : 'categories/PRODUCT_STATUS',
    'PRODUCT_TYPE_URI' : 'categories/PRODUCT_TYPE',
    'POL_CREATION_TYPE_MANUAL' : 'categories/POH_CREATION_TYPE/POH_CREATION_TYPE_MANUAL',
    'POL_CREATION_TYPE_AUTO' : 'categories/POH_CREATION_TYPE/POH_CREATION_TYPE_AUTO',
    'POH_STATUS_IN_PROGRESS' : 'categories/POH_STATUS/POH_STATUS_IN_PROGRESS',
    'BOQ_LINE_STATUS_VOID' : 'categories/BOQ_LINE_STATUS/BOQ_LINE_STATUS_VOID',
    'BOQ_LINE_STATUS_PENDING' : 'categories/BOQ_LINE_STATUS/BOQ_LINE_STATUS_PENDING',
    'POH_STATUS_URI' : 'categories/POH_STATUS',
    'DLV_NOTE_STATUS_URI' : 'categories/DLV_NOTE_STATUS'
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


cimgApp.service('configService', function (SERVER,PORT,WEBAPP) {
    var address = 'http://' + SERVER+':'+ PORT +'/' +WEBAPP + '/rest/'

    return {
        getAddress: function () {
            return address
        }
    };

});

cimgApp.service('baseDataService', function ($location, $http, $window,ngDialog, configService) {

    var row;
    var rowId;
    var isPageNew = true;
    var rowSelected = false;

    return {

        setRow: function (myRow) {
            row = myRow;
        },
        getRow: function () {
            return row;
        },
        setRowId: function (myRowId) {
            rowId = myRowId;
        },
        getRowId: function () {
            return rowId;
        },
        setRowSelected: function (selected) {
            rowSelected = selected;
        },
        getRowSelected: function () {
            return rowSelected;
        },
        getIsPageNew: function () {
            return isPageNew;
        },

        setIsPageNew: function (isNew) {
            isPageNew=isNew;
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
        populateMultiSelectList: function (selectName, sourceData) {
            if (selectName==null)
                return;
            var lineArr = sourceData;
            var newLines=[];
            var selectedLines = selectName;
            if (selectedLines.length <1) {
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
        displayMessage : function (heading,mBody) {

            var myMessage = {
                'title': heading,
                'body' : mBody,
                 'type': 'info'
            }
            ngDialog.openConfirm({
                template:'views/pages/messageDialog.html',
                controller:'messageDialogCtrl',
                className: 'ngdialog-theme-plain custom-width',
                closeByDocument:false,
                resolve: {message: function(){return myMessage}}
            }).then (function (msg){
                    if (msg != undefined) {
                    }
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
            /*
            ngDialog.open({
                template: '<h1>'+heading+'</h1><h1> <p>' + message + '</p>',
                closeByDocument: false,
                plain: true
            });
            */
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
    }


}

});

cimgApp.service('incidentService', function ($location, $http, configService) {
    var isPageNew = true; //default is new page
    var incident = {};
    return {

        getIncident: function () {
            return incident;
        },

        setIncident:function(row){
          incident = row;
        },
        getIsPageNew: function () {
            return isPageNew;
        },

        setIsPageNew: function (isNew) {
            isPageNew=isNew;
        },

        // populating script list
        synchScriptModel: function (scriptList,$scope) {
            if (scriptList == undefined || scriptList == null)
                return;
            for (var i = 0; i < scriptList.length; i++) {
                this.synchScriptItemList (scriptList[i], $scope);
            }
        },
        synchScriptItemList: function (scriptNode,$scope) {

            var itemList = scriptNode.scriptItemList;
            var scriptName = scriptNode.name;
            var itemSelected=false;
            var elementId;
            var itemOrder;
            var itemId;
            var wholeScriptText='';
            if (itemList == undefined || itemList == null)
                return;
            if (scriptName === 'notification_script_reason') {
                    //itemList[0].selected = true;
                    itemList[0].paramList[0].valueNumeric = $scope.incident.reason.id;
                    itemList[0].paramList[0].paramText = $scope.incident.reason.description;

                    //itemList[i].selected = true;
                    itemList[0].paramList[1].valueNumeric = $scope.incident.dueTo.id;
                    itemList[0].paramList[1].paramText = $scope.incident.dueTo.name;
                //var preparedScriptText = itemList[0].scriptText.replace(itemList[0].paramList[0].name,itemList[0].paramList[0].paramText);
                //preparedScriptText = preparedScriptText.replace(itemList[0].paramList[1].name,itemList[0].paramList[1].paramText);
                // we only need the due to text
                scriptNode.preparedScriptText = $scope.incident.dueTo.name;
                return;
            }
            for (var i = 0; i < itemList.length; i++) {

                //continue if no buss has been selected.
                /*
                if (scriptName ==='notification_script_buss' && i===0)
                    continue;
                 */
                itemOrder = itemList[i].order;
                itemId= itemOrder - 1;
                elementId = scriptName + '_' +itemId;
                if (document.getElementById(elementId) !=null ) {
                    itemSelected = document.getElementById(elementId).checked;
                    itemList[i].selected = itemSelected;
                }
                this.synchItemParamList (scriptName,itemList[i],$scope);
                if (itemList[i].selected){
                    wholeScriptText = wholeScriptText +itemList[i].preparedText+' ';
                }
            }
            scriptNode.preparedScriptText = wholeScriptText;
        },
        synchItemParamList: function (scriptName, itemNode,$scope) {

            var paramList = itemNode.paramList;
            var elementId;
            var paramOrder;
            var paramId;
            var paramType;
            var preparedItemText = itemNode.scriptText;
            var itemId = itemNode.order -1;
            var replacementText;
            if (paramList == undefined || paramList == null || paramList.length==0) {
                if (preparedItemText.indexOf("No ") >-1 ) {
                    itemNode.preparedText ='';
                } else
                    itemNode.preparedText = preparedItemText;
                return;
            }
            for (var i = 0; i < paramList.length; i++) {
                paramOrder = paramList[i].order;
                paramId= paramOrder - 1;
                elementId = scriptName + '_' +itemId+'_'+paramId;
                var element = document.getElementById(elementId);
                if (element !=null ) {
                    paramType = paramList[i].type;
                    if (paramType === 'select') {
                        var itemVal = element.options[element.selectedIndex].value
                        paramList[i].valueNumeric = itemVal;
                        paramList[i].paramText = element.options[element.selectedIndex].text;
                    }
                    if (paramType === 'text' || paramType === 'textArea' ) {
                        itemVal = element.value;
                        paramList[i].valueText = itemVal;
                        paramList[i].paramText = itemVal;
                    }
                    // prepare script text for item
                    if (itemNode.selected) {
                        replacementText = paramList[i].name;
                        preparedItemText = preparedItemText.replace(replacementText,paramList[i].paramText);

                    }
                }
            }
            itemNode.preparedText = preparedItemText;
        },
        getWholeScriptText: function (scriptList, scriptName) {
            if (scriptList == undefined || scriptList == null)
                return '';
            for (var i = 0; i < scriptList.length; i++) {
                if (scriptList[i].name === scriptName)
                    return scriptList[i].preparedScriptText;
            }
        },
        stripeScriptModel: function (scriptList) {
            var itemList;
            if (scriptList == undefined || scriptList == null)
                return;
            for (var i = 0; i < scriptList.length; i++) {
                scriptList[i].preparedScriptText='';
                itemList = scriptList[i].scriptItemList;
                if (itemList == undefined || itemList == null)
                    continue;
                for (var j = 0; j < itemList.length; j++) {
                    itemList[j].htmlScriptText='';
                    itemList[j].scriptText='';
                }
            }
        }
        // end of populating script list

    }
});


cimgApp.service('UserInfo', function () {
    /*userType must be user,admin or reader*/
    var message;
    return {
        getMessage: function () {
            return message;
        },
        setMessage: function (msg) {
            message = msg;
        }
    };
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

cimgApp.service('UserService', function ($window, $location,$http) {
    return {
        setUser: function (credentials, token, userId) {
            $window.sessionStorage.userType = credentials;
            $window.sessionStorage.userName = token;
            $window.sessionStorage.userId = userId;
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
        getUserId: function(){
            return $window.sessionStorage.userId;
        },
        getUserName: function(){
            return $window.sessionStorage.userName;
        }
    };
});

cimgApp.service('AccessChecker', function ($state, $window, UserService, UserInfo) {
    /*userType must be user,admin or reader*/
    return {
        checkAcess: function (level) {

            if (level == 'user' && $window.sessionStorage.userType == 'reader') {
                //$location.path('/login');
                UserInfo.setMessage('Access Denied. Please login as a CIU operator')
                $state.go('dashboard.login');
            } else if (level == 'admin' && $window.sessionStorage.userType != 'admin') {
                //$location.path('/login');
                UserInfo.setMessage('Access Denied. Please login as Admin')
                $state.go('dashboard.login');
            }
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
cimgApp.filter('configCategoryFilter', function() {
    return function (status) {
        return status.displayName;
    };
});
    cimgApp.filter('fullName', function() {
        return function (lastModifiedBy) {
            if (lastModifiedBy ==undefined || lastModifiedBy ==null) {
                return '';
            }
            return lastModifiedBy.firstName + ' ' + lastModifiedBy.sureName;
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
