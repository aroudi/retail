/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailCtrl', function($scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE, POL_CREATION_TYPE_MANUAL) {

    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'25%'},
            {field:'purchaseItem.partNo', displayName:'Part No', enableCellEdit:false, width:'25%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'10%'},
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polQtyOrdered', displayName:'Qty',enableCellEdit:true, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polValueOrdered', displayName:'Value',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polCreationType', displayName:'Type',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }
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
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'polQtyOrdered') {
                $scope.polQtyOrderedBeforeEditting = rowEntity.polQtyOrdered;
            }
        })
    };
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var purchaseLine = event.targetScope.row.entity;
        cellData = event.targetScope.row.entity[event.targetScope.col.field];
        if (purchaseLine.polCreationType.displayName=='AUTO') {
            if (cellData < $scope.polQtyOrderedBeforeEditting) {
                baseDataService.displayMessage('Warning!!','You can not decrease quantity for auto created item.');
                purchaseLine.polQtyOrdered = $scope.polQtyOrderedBeforeEditting;
                return;
            }
        }
    })

    initPageData();
    function initPageData() {
        $scope.purchaseOrderHeader = angular.copy(baseDataService.getRow());
        $scope.gridOptions.data = $scope.purchaseOrderHeader.lines;
        baseDataService.setRow({});

        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            var data = angular.copy(response.data);
            $scope.polCreationTypeManual = data;
        });

    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    $scope.searchProduct = function () {
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productPurchaseItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {supplier: function(){return $scope.purchaseOrderHeader.supplier}}
        }).then (function (selectedItems){
                if (selectedItems != undefined) {
                    for (var i = 0; i < selectedItems.length; i++) {
                        var selectedProduct = selectedItems[i];
                        var purchaseLine = createPurchaseLine(selectedProduct);
                        $scope.gridOptions.data.push(purchaseLine);
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    function createPurchaseLine (item) {
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var purchaseLineObject = {
            'id':rowId,
            'pohId':$scope.purchaseOrderHeader.id,
            'pohOrderNumber':$scope.purchaseOrderHeader.pohOrderNumber,
            'purchaseItem' : item,
            'polUnitCost' : item.price,
            'unitOfMeasure' : item.unitOfMeasure,
            'polQtyOrdered' : 0.00,
            'polValueOrdered' : 0.00,
            'unomContents' : item.unomQty,
            'polCreationType' :  $scope.polCreationTypeManual
        }
        return purchaseLineObject;
    }
});
