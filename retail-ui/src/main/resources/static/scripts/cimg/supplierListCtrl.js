/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('supplierListCtrl', function($scope, $state, $timeout,ngDialog, UserService, baseDataService, SUCCESS, FAILURE, SUPPLIER_ALL_URI, SUPPLIER_GET_URI, SUPPLIER_LOGICAL_DELETE_URI) {
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
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    $scope.gridOptions.noUnselect= false;

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

    $scope.isRowSelected = function(){
        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
            return true;
        }
        return false;
    }

    $scope.deleteSupplierLogically = function () {
        //check if customer has access
        if (!UserService.checkUserHasAccess("deleteSupplier")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have delete access");
            return;
        }

        var selectedSupplierIdList = [];
        if ($scope.gridApi.selection.getSelectedRows() === undefined || $scope.gridApi.selection.getSelectedRows().length < 1){
            baseDataService.displayMessage('info', 'rows not selected', 'please select item/s to delete');
            return;
        }
        baseDataService.displayMessage('yesNo','Confirmation required!!','Do you want to delete selected rows?').then(function(result){
            if (result) {
                for (var i = 0; i < $scope.gridApi.selection.getSelectedRows().length; i++) {
                    selectedSupplierIdList.push($scope.gridApi.selection.getSelectedRows()[i].id);
                }
                baseDataService.addRow(selectedSupplierIdList, SUPPLIER_LOGICAL_DELETE_URI).then(function(response) {
                    var res = response.data;
                    if (res.status != SUCCESS ) {
                        baseDataService.displayMessage('info', 'warning', 'Not able to delete supplier. message:' + res.message);
                    } else {
                        for (var i = 0; i < $scope.gridApi.selection.getSelectedRows().length; i++) {
                            var itemIndex = baseDataService.getArrIndexOf($scope.gridOptions.data, $scope.gridApi.selection.getSelectedRows()[i]);
                            console.log('index= ' + itemIndex);
                            if (itemIndex > -1) {
                                $scope.gridOptions.data.splice(itemIndex, 1);
                            }
                            $scope.gridApi.selection.unSelectRow($scope.gridApi.selection.getSelectedRows()[i]);
                            //$scope.gridApi.core.setRowInvisible($scope.gridApi.selection.getSelectedRows()[i]);
                        }
                    }
                });
            } else {
                return;
            }
        });
    }
    $scope.createSupplier = function () {
        //check if customer has access
        if (!UserService.checkUserHasAccess("createSupplier")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have access for creating supplier");
            return;
        }
        $state.go('dashboard.createSupplier', {blankPage:true});

    }
});
