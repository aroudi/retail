/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productPurchaseOrderListCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, productId, SUCCESS, FAILURE, PRODUCT_GET_PO_LIST_URI ) {

    initPurchaseOrderList();
    getAllProductPurchaseOrders();
    function initPurchaseOrderList() {
        $scope.purchaseOrderList = {
            enableFiltering: true,
            enableRowSelection:true,
            showGridFooter: true,
            showColumnFooter: true,
            enableColumnResizing: true,
            enableSorting:true,
            expandableRowTemplate: '<div ui-grid="row.entity.subpurchaseOrderList" ui-grid-selection style ="height: 100px;"></div>',
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'pohCreatedDate', displayName:'Date',enableCellEdit:false, width:'15%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'15%'},
                {field:'pohPrefix', displayName:'Suffix',enableCellEdit:false, width:'10%'},
                {field:'supplierName', displayName:'Supplier',enableCellEdit:false, width:'25%'},
                {field:'polUnitCost', displayName:'Cost',enableCellEdit:false, width:'10%',cellFilter: 'currency'},
                {field:'polQtyOrdered', displayName:'Qty',enableCellEdit:false, width:'10%'},
                {field:'polStatus', displayName:'status',enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
            ]
        }
        $scope.purchaseOrderList.enableRowSelection = true;
        $scope.purchaseOrderList.multiSelect = false;

        //
        $scope.purchaseOrderList.onRegisterApi = function (gridApi) {
            $scope.purchaseOrderListGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
        };
    }
    function getAllProductPurchaseOrders() {
        baseDataService.getBaseData(PRODUCT_GET_PO_LIST_URI + productId).then(function(response){
            $scope.purchaseOrderList.data = response.data;
        });
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
