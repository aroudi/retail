/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('supplierSearchCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, SUPPLIER_ALL_URI, SUPPLIER_GET_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'supplierCode', enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.supplierCode
                }
            },
            {field:'supplierName', enableCellEdit:false, width:'50%',
                cellTooltip: function(row,col) {
                    return row.entity.supplierName
                }
            },
            {field:'supplierType.displayName', displayName:'Supplier Type',enableCellEdit:false, width:'10%'},
            {field:'supplierStatus.displayName', displayName:'Status',enableCellEdit:false, width:'10%'}
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
    getAllSuppliers();
    function getAllSuppliers() {
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
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
