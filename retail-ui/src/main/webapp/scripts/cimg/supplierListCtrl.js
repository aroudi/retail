/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('supplierListCtrl', function($scope, $state, $timeout,ngDialog, baseDataService, SUCCESS, FAILURE, SUPPLIER_ALL_URI, SUPPLIER_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Action',enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editSupplier(row, false)"></i></a>&nbsp;&nbsp;<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.editSupplier(row, true)"></i></a>', width:'5%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'supplierCode', enableCellEdit:false, width:'20%'},
            {field:'supplierName', enableCellEdit:false, width:'45%',
                cellTooltip: function(row,col) {
                    return row.entity.supplierName
                }
            },
            {field:'contact.phone', displayName:'Phone',enableCellEdit:false, width:'10%'},
            {field:'contact.fax', displayName:'Fax',enableCellEdit:false, width:'10%'},
            //{field:'supplierType.displayName', displayName:'Supplier Type',enableCellEdit:false, width:'10%'},
            {field:'suppOrguLink.supplierStatus.displayName', displayName:'Status',enableCellEdit:false, width:'10%'}
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

    $scope.editSupplier = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var supplierGetURI = SUPPLIER_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(supplierGetURI).then(function (response) {
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);

            if (viewMode) {
                ngDialog.openConfirm({
                    template:'views/pages/supplier.html',
                    controller:'supplierCtrl',
                    className: 'ngdialog-pdfView',
                    closeByDocument:false,
                    resolve: {viewMode: function(){return true}
                    }
                }).then (function (){
                    }, function(reason) {
                        console.log('Modal promise rejected. Reason:', reason);
                    }
                );
            } else {
                //redirect to the supplier page.
                $state.go('dashboard.createSupplier');
            }
        });

    }
});
