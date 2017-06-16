/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailAddLineCtrl', function($scope, $timeout,ngDialog, uiGridConstants, baseDataService, SUCCESS, FAILURE, GET_PURCHASE_ITEMS_PER_SUPPLIER_URI, POL_CREATION_TYPE_MANUAL, BOQ_LINE_STATUS_PENDING) {

    initPageData();
    function initPageData() {
        $scope.boqDetail = {};
        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            $scope.boqDetail.bqdCreationType = response.data;
        });
        baseDataService.getBaseData(BOQ_LINE_STATUS_PENDING).then(function(response){
            $scope.boqDetail.bqdStatus = response.data;
        });
    }



    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'solId', visible:false, enableCellEdit:false},
            {field:'prodId', visible:false, enableCellEdit:false},
            {field:'catalogueNo', enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.catalogueNo
                }
            },
            /*{field:'partNo', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.partNo
                }
            },*/
            {field:'unitOfMeasure.unomDesc', displayName:'Size',enableCellEdit:false, width:'10%'},
            {field:'price', displayName:'price',enableCellEdit:false, width:'10%'},
            {field:'quantity', displayName:'Qty',enableCellEdit:true, width:'10%',type: 'number',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'qtyOnStock', displayName:'In Stock Qty',enableCellEdit:true, width:'10%',type: 'number',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'comment', displayName:'In Stock Location', enableCellEdit:true, width:'11%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'qtyBalance', displayName:'Balance',enableCellEdit:false, width:'7%',type: 'number',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'changeComment', displayName:'Comments', enableCellEdit:true, width:'19%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            }
        ]
    }
    $scope.gridOptions.multiSelect = true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'quantity') {
                $scope.quantityBeforeEditting = rowEntity.quantity;
            }
            if (colDef.name == 'qtyOnStock') {
                $scope.qtyOnStockBeforeEditting = rowEntity.qtyOnStock;
            }
        });
    };
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var rowEntity = event.targetScope.row.entity;
        if (rowEntity.qtyOnStock == undefined) {
            rowEntity['qtyOnStock'] = 0;
        }
        if (rowEntity.quantity == undefined) {
            rowEntity['quantity'] = 0;
        }
        var balance =rowEntity.quantity*1 - rowEntity.qtyOnStock*1;
        if (balance < 0) {
            baseDataService.displayMessage('Warning','Invalid Qty', 'Negative balance');
            rowEntity['quantity'] = $scope.quantityBeforeEditting;
            rowEntity['qtyOnStock'] = $scope.qtyOnStockBeforeEditting;
            balance =rowEntity.quantity*1 - rowEntity.qtyOnStock*1;
        }
        rowEntity['qtyBalance'] = balance;
    });
    //getAllProductPurchaseItemsPerSupplier();
    $scope.getProductsPerSupplier = function() {
        var supplier = $scope.supplier;
        baseDataService.getBaseData(GET_PURCHASE_ITEMS_PER_SUPPLIER_URI+supplier.id).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }
    $scope.submit = function () {

        var selectedItemList = $scope.gridApi.selection.getSelectedRows();
        if (selectedItemList.length < 1) {
            baseDataService.displayMessage('info','No item', 'No item is selected.');
            return;
        }
        var boqList = [];
        for (var i=0; i < selectedItemList.length; i++) {
            var selectedItem = selectedItemList[i];

            var product = {
                id : selectedItem.prodId,
                prodSku : selectedItem.catalogueNo
            }
            var boqDetailObject = {
                'id':-1,
                'supplier':$scope.supplier,
                'product':product,
                'unitOfMeasure' : selectedItem.unitOfMeasure,
                'cost' : selectedItem.price,
                'quantity' : selectedItem.quantity,
                'itemValue' : selectedItem.quantity*selectedItem.price ,
                'qtyOnStock' : selectedItem.qtyOnStock,
                'comment' : selectedItem.comment,
                'changeComment' : selectedItem.changeComment,
                'qtyBalance' :  selectedItem.qtyBalance,
                'bqdCreationType' :  $scope.boqDetail.bqdCreationType,
                'bqdStatus' :  $scope.boqDetail.bqdStatus,
                'qtyPurchased' : 0.00,
                'qtyReceived' : 0.00
            }
            boqList.push(boqDetailObject);
        }
        $scope.confirm(boqList);
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

    $scope.calculateBalance = function() {
        $scope.boqDetail.qtyBalance = $scope.boqDetail.quantity - $scope.boqDetail.qtyOnStock;
    }

    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.supplier = value;
                $scope.getProductsPerSupplier();
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

});
