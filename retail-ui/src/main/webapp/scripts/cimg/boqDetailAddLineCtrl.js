/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailAddLineCtrl', function($scope, $timeout,ngDialog, uiGridConstants, baseDataService, SUCCESS, FAILURE, GET_PURCHASE_ITEMS_PER_SUPPLIER_URI, POL_CREATION_TYPE_MANUAL, BOQ_LINE_STATUS_PENDING) {

    initPageData();
    function initPageData() {
        $scope.boqDetail = {};
        $scope.boqDetail.quantity = 0.0;
        $scope.boqDetail.qtyOnStock = 0.0;
        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            $scope.boqDetail.bqdCreationType = response.data;
        });
        baseDataService.getBaseData(BOQ_LINE_STATUS_PENDING).then(function(response){
            $scope.boqDetail.bqdStatus = response.data;
        });
    }



    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
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
            {field:'partNo', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.partNo
                }
            },
            {field:'unitOfMeasure.unomDesc', displayName:'Size',enableCellEdit:false, width:'10%'},
            {field:'price', displayName:'price',enableCellEdit:false, width:'10%'},
            {field:'bulkPrice', displayName:'bulkPrice',enableCellEdit:false, width:'10%'},
            {field:'bulkQty', displayName:'bulkQty',enableCellEdit:false, width:'10%'},
            {field:'sprcMinOrdQty', displayName:'Min Order',enableCellEdit:false, width:'10%'}
        ]
    }
    $scope.gridOptions.multiSelect = false;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
        });
    };




    //getAllProductPurchaseItemsPerSupplier();
    $scope.getProductsPerSupplier = function() {
        var supplier = $scope.supplier;
        baseDataService.getBaseData(GET_PURCHASE_ITEMS_PER_SUPPLIER_URI+supplier.id).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }
    $scope.submit = function () {
        if ($scope.selectedOption != undefined) {
            var product = {
                id : $scope.selectedOption.prodId,
                prodSku : $scope.selectedOption.catalogueNo
            }
            var boqDetailObject = {
                'id':-1,
                'supplier':$scope.supplier,
                'product':product,
                'unitOfMeasure' : $scope.selectedOption.unitOfMeasure,
                'cost' : $scope.selectedOption.price,
                'quantity' : $scope.boqDetail.quantity,
                'itemValue' : $scope.boqDetail.quantity*$scope.selectedOption.price ,
                'qtyOnStock' : $scope.boqDetail.qtyOnStock,
                'comment' : $scope.boqDetail.comment,
                'changeComment' : $scope.boqDetail.changeComment,
                'qtyBalance' :  $scope.boqDetail.qtyBalance,
                'bqdCreationType' :  $scope.boqDetail.bqdCreationType,
                'bqdStatus' :  $scope.boqDetail.bqdStatus,
                'qtyPurchased' : 0.00,
                'qtyReceived' : 0.00
            }
            $scope.confirm(boqDetailObject);
        }
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
