/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerSearchCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CUSTOMER_ALL_URI, CUSTOMER_GET_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'customerId', visible:false, enableCellEdit:false},
            {field:'customerType', displayName:'Type',enableCellEdit:false, width:'10%'},
            {field:'code', enableCellEdit:false, width:'10%'},
            {field:'firstName', displayName:'First Name',enableCellEdit:false, width:'10%'},
            {field:'surName', displayName:'Sur Name',enableCellEdit:false, width:'10%'},
            {field:'companyName', displayName:'Company Name',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.companyName
                }
            },
            {field:'mobile', enableCellEdit:false, width:'10%'},
            {field:'address', enableCellEdit:false , width:'30%'},
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
            $scope.selectedOption = row.entity;
        });
    };
    getAllCustomers();
    function getAllCustomers() {
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }
    $scope.submit = function () {
        if ($scope.selectedOption != undefined) {
            $scope.confirm($scope.selectedOption);
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
