/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('supplierListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, SUPPLIER_ALL_URI, SUPPLIER_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editSupplier(row)"></i></a>', width:'5%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'supplierCode', enableCellEdit:false, width:'20%'},
            {field:'supplierName', enableCellEdit:false, width:'50%',
                cellTooltip: function(row,col) {
                    return row.entity.supplierName
                }
            },
            {field:'supplierType.displayName', displayName:'Supplier Type',enableCellEdit:false, width:'10%'},
            {field:'supplierStatus.displayName', displayName:'Status',enableCellEdit:false, width:'10%'}
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
    };
    getAllSuppliers();
    function getAllSuppliers() {
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editSupplier = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var supplierGetURI = SUPPLIER_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(supplierGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createSupplier');
        });
    }
});
