/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerCtrl', function($scope, $state,ngDialog, UserService, baseDataService, SUCCESS, FAILURE, CUSTOMER_ADD_URI, CUSTOMERGRADE_ALL_URI, CUSTOMER_TYPE_URI) {
    //set default data on the page
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'contactType', displayName:'Type',enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'title', displayName:'Title', enableCellEdit:false, width:'5%'},
            {field:'firstName', displayName:'First Name',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.firstName
                }
            },
            {field:'surName', displayName:'Sur Name',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.surName
                }
            },
            {field:'mobile', enableCellEdit:false, width:'10%'},
            {field:'email', enableCellEdit:false, width:'20%'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editContact(row)"></i></a><a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeContact(row)"></i></a>', width:'5%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };



    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.customer = {};
            $scope.customer.customerType='Individual';
        } else {
            $scope.customer = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.customer.contacts;
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
        }
        baseDataService.getBaseData(CUSTOMERGRADE_ALL_URI).then(function(response){
            $scope.gradeSet = response.data;
            $scope.customer.grade = baseDataService.populateSelectList($scope.customer.grade,$scope.gradeSet);
        });
        baseDataService.getBaseData(CUSTOMER_TYPE_URI).then(function(response){
            $scope.customerTypeSet = response.data;
            $scope.customer.customerType = baseDataService.populateSelectList($scope.customer.customerType,$scope.customerTypeSet);
        });
    }

    //create new customer
    $scope.createCustomer = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        $scope.customer.contacts = $scope.gridOptions.data;
        var rowObject = $scope.customer;
        baseDataService.addRow(rowObject, CUSTOMER_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listCustomer');
            } else {
                alert('Not able to save customer. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

    $scope.addContact = function () {
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        ngDialog.openConfirm({
            template:'views/pages/contact.html',
            controller:'addContactCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {contactObject: function(){return {}}}
        }).then (function (updatedContact){
                if (updatedContact != undefined) {
                    //row = updatedContact;
                    updatedContact.id = rowId;
                    $scope.gridOptions.data.push(updatedContact);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }
    $scope.editContact = function (row) {
        var contactBackUp = angular.copy(row.entity);
        var contactObject = row.entity;

        ngDialog.openConfirm({
            template:'views/pages/contact.html',
            controller:'addContactCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {contactObject: function(){return contactObject}}
        }).then (function (updatedContact){
                if (updatedContact != undefined) {
                    row.entity = updatedContact;
                }
            }, function(reason) {
                row.entity = contactBackUp;
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }
    $scope.removeContact = function(row) {
        if (!confirm('Are you sure you want to delete this contact?')) {
            return;
        }
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        var rowIndex = baseDataService.getArrIndexOf($scope.gridOptions.data, row.entity);
        if (rowIndex>-1) {
            $scope.gridOptions.data.splice(rowIndex,1);
        }
    }

});
