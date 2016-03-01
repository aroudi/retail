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
    'treeControl'
  ]);

/*
var config_data = {
    'SERVER' : 'asmet568',
    'PORT'   : '8189',
    'WEBAPP' :'cimg-web-services'
}
*/
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
    'SUPPLIER_GET_URI' : 'supplier/get'
    //
}

var response_status = {
    'SUCCESS' : 1,
    'FAILURE' : 0
}

var type_constant = {
    'SUPPLIER_TYPE_URI' : 'categories/SUPPLIER_TYPE',
    'SUPPLIER_STATUS_URI' : 'categories/SUPPLIER_STATUS'
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

cimgApp.service('baseDataService', function ($location, $http, $window, configService) {

    var row;
    var isPageNew = true;
    var rowSelected = false;

    return {

        setRow: function (myRow) {
            row = myRow;
        },
        getRow: function () {
            return row;
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
cimgApp.filter('accessibilityStatus', function() {
    return function (status) {
        if (status == true) {
            return 'Good'
        } else {
            return 'Faulty'
        }
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

