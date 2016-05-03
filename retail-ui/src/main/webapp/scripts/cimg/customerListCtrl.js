/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CUSTOMER_ALL_URI, CUSTOMER_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'customerId', visible:false, enableCellEdit:false},
            {field:'customerType', displayName:'Type',enableCellEdit:false, width:'8%'},
            {field:'code', enableCellEdit:false, width:'7%'},
            {field:'firstName', displayName:'First Name',enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.firstName
                }
            },
            {field:'surName', displayName:'Sur Name',enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.surName
                }
            },
            {field:'companyName', displayName:'Company Name',enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.companyName
                }
            },
            {field:'mobile', enableCellEdit:false, width:'10%'},
            {field:'address', enableCellEdit:false , width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.address
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editCustomer(row)"></i></a>', width:'10%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };
    getAllCustomers();
    function getAllCustomers() {
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editCustomer = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var customerGetURI = CUSTOMER_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(customerGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the customer page.
            $state.go('dashboard.createCustomer');
        });
    }
});
