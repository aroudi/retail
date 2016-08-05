/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleCtrl', function($scope, $state, $timeout, $stateParams, baseDataService,ngDialog, uiGridConstants, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI, TXN_ADD_URI, TXN_TYPE_QUOTE, TXN_TYPE_SALE, TXN_STATE_FINAL, TXN_STATE_DRAFT) {

    $scope.isPageNew = baseDataService.getIsPageNew();
    initPageData();
    initTxnDetail();
    initTxnMediaList();

    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            //get txn_type from state params.
            $scope.txnType = $stateParams.txnType;
            $scope.txnHeaderForm = {};
            $scope.txnHeaderForm.id = -1;
        } else {
            $scope.txnHeaderForm = angular.copy(baseDataService.getRow());
            $scope.customer = $scope.txnHeaderForm.customer;
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
        }
        $scope.txnHeaderForm.convertedToTxnSale = false;
        if ($scope.isPageNew) {
            baseDataService.getBaseData(TXN_TYPE_QUOTE).then(function(response){
                if ($scope.txnType == 'quote') {
                    $scope.txnHeaderForm.txhdTxnType = response.data;
                }
            });
            baseDataService.getBaseData(TXN_TYPE_SALE).then(function(response){
                if ($scope.txnType == 'txnSale') {
                    $scope.txnHeaderForm.txhdTxnType = response.data;
                }
            });
            //DEFUALT STATE IS DRAFT.
            baseDataService.getBaseData(TXN_STATE_DRAFT).then(function(response){
                $scope.txnHeaderForm.txhdState = response.data;
            });
        }
        baseDataService.getBaseData(MEDIA_TYPE_ALL_URI).then(function(response){
            $scope.mediaTypeSet = response.data;
            $scope.mediaType = baseDataService.populateSelectList($scope.mediaType,$scope.mediaTypeSet);
            $scope.onMediaTypeChange();
        });
    }

    $scope.onMediaTypeChange = function () {
        var paymentMediaOfTypeUri = PAYMENT_MEDIA_OF_TYPE_URI + $scope.mediaType.id;
        baseDataService.getBaseData(paymentMediaOfTypeUri).then(function(response){
            $scope.paymentMediaSet = response.data;
            $scope.paymentMedia = baseDataService.populateSelectList($scope.paymentMedia,$scope.paymentMediaSet);
        });
    }

    function initTxnDetail() {
        var rowtpl='<div ng-class="{\'blue\':row.entity.txdeItemVoid==false, \'red\':row.entity.txdeItemVoid==true }"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
        $scope.txnDetailList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter: true,
            gridFooterTemplate:"<div id=\"currency-default\"> Total:{{grid.appScope.txnHeaderForm.txhdValueNett | currency}}</div>",
            rowTemplate : rowtpl,
            columnDefs: [
                {field: 'id', visible: false, enableCellEdit: false},
                {field: 'product.prodSku', displayName: 'SKU', enableCellEdit: false, width: '5%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodSku
                    }
                },
                {field: 'product.prodName', displayName: 'Name', enableCellEdit: false, width: '30%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodName
                    }
                },
                {field: 'unitOfMeasure.unomDesc', displayName: 'Size', enableCellEdit: false, width: '5%'},
                {field: 'txdeValueLine', displayName: 'Cost', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'txdeValueProfit', displayName: 'Profit', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'txdeValueGross', displayName: 'Gross Value', cellFilter: 'currency', width: '8%'},
                {field: 'txdeTax', displayName: 'Tax', enableCellEdit: false, width: '5%'},
                {field: 'txdeValueNet', displayName: 'Nett Value', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'txdeQuantitySold', displayName: 'Qty', type: 'number', width: '5%'},
                {field: 'txdePriceSold', displayName: 'Total', cellFilter: 'currency', footerCellFilter: 'currency', enableCellEdit: false, width: '10%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Item" ng-show="grid.appScope.isTxnLineVoidable()" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidItem(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" ng-show="row.entity.id < 0" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a>', width: '8%'}

            ]
        }
        $scope.txnDetailList.enableRowSelection = true;
        $scope.txnDetailList.multiSelect = false;
        $scope.txnDetailList.noUnselect = true;
        //
        $scope.txnDetailList.onRegisterApi = function (gridApi) {
            $scope.txnDetailGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                $scope.selectedTxnDetailRow = row.entity;
            });
        };
        $scope.$on('uiGridEventEndCellEdit', function (event) {
            var txnDetail = event.targetScope.row.entity;
            cellData = event.targetScope.row.entity[event.targetScope.col.field];
            //txnDetail.txdeQuantitySold = cellData;
            txnDetail.txdeValueNet =  (txnDetail.txdeValueGross * txnDetail.txdeTax)*1 + txnDetail.txdeValueGross*1;
            //alert('gross =' + txnDetail.txdeValueGross + ' --tax rate = '  + txnDetail.txdeTax + ' -- net value = ' + txnDetail.txdeValueNet );
            txnDetail.txdePriceSold = txnDetail.txdeQuantitySold * txnDetail.txdeValueNet;
            totalTransaction();
        })
        if (!$scope.isPageNew ) {
            $scope.txnDetailList.data = angular.copy($scope.txnHeaderForm.txnDetailFormList);
        }
    }


    function initTxnMediaList() {
        var tenderTpl='<div ng-class="{\'blue\':row.entity.txmdVoided==false, \'red\':row.entity.txmdVoided==true }"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
        $scope.txnMediaList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter:true,
            gridFooterTemplate:"<div id=\"currency-default\"> Total:{{grid.appScope.calculateAmountPaid() | currency}}</div>",
            rowTemplate : tenderTpl,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'paymentMedia.paymName',displayName:'Payment Media', visible:true, enableCellEdit:false, width: '50%',
                    cellTooltip: function(row,col) {
                        return row.entity.paymentMedia.paymName
                    }
                },
                {field:'txmdAmountLocal', displayName:'Amount', visible:true, cellFilter:'currency', footerCellFilter:'currency', /*aggregationType: uiGridConstants.aggregationTypes.sum, */ enableCellEdit:false, width: '40%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Tender" ng-show="grid.appScope.isTxnLineVoidable()" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidTender(row)" ></i></a>', width: '10%'}

            ]
        }
        $scope.txnMediaList.enableRowSelection = false;
        $scope.txnMediaList.multiSelect = false;
        $scope.txnMediaList.noUnselect= true;

        //
        $scope.txnMediaList.onRegisterApi = function (gridApi) {
            $scope.txnMediaGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                $scope.selectedTxnMediaRow = row.entity;
            });
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
        };

        if (!$scope.isPageNew ) {
            $scope.txnMediaList.data = $scope.txnHeaderForm.txnMediaFormList;
        }
    }

    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                $scope.customer = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.searchProduct = function () {
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productSaleItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (selectedItems){
                //alert('returned value = ' + value);
                if (selectedItems != undefined) {
                    for (var i = 0; i < selectedItems.length; i++) {
                        var selectedProduct = selectedItems[i];
                        var txnDetail = createTxnDetail();
                        txnDetail.product = selectedProduct;
                        txnDetail.unitOfMeasure = txnDetail.product.sellPrice.unitOfMeasure;
                        evaluatRowItem(txnDetail)
                        $scope.txnDetailList.data.push(txnDetail);
                        totalTransaction();
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    
    
    function createTxnDetail () {
        var rowId;
        if ($scope.txnDetailList.data == undefined && $scope.txnDetailList.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.txnDetailList.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var txnDetailObject = {
            'id':rowId,
            'product' : {},
            'txdePriceOveriden' : false,
            'unitOfMeasure' : {},
            'txdePriceOveriden' : false,
            'txdeLineRefund' : false,
            'txdeItemVoid' : false,
            'deleted' : false
        }
        return txnDetailObject;
    }
    function evaluatRowItem (txnDetail) {
        txnDetail.txdeTax = calculateTaxRate(txnDetail.product);
        txnDetail.txdeValueLine = txnDetail.product.sellPrice.prcePrice;
        txnDetail.txdeProfitMargin = getProfitMargin();
        txnDetail.txdeValueProfit =  txnDetail.txdeValueLine*txnDetail.txdeProfitMargin;
        txnDetail.txdeValueGross =  txnDetail.txdeValueProfit*1 + txnDetail.txdeValueLine*1;
        txnDetail.txdeValueNet =  (txnDetail.txdeValueGross * txnDetail.txdeTax)*1 + txnDetail.txdeValueGross*1;
        txnDetail.txdeQuantitySold =  1;
        txnDetail.txdePriceSold =  txnDetail.txdeQuantitySold * txnDetail.txdeValueNet; 
    }

    function calculateTaxRate (product) {
        if (product == undefined || product == null) {
            return 0.00;
        }
        var taxRules = product.prodOrguLink.taxRules;
        var taxValue = 0.00;
        for (var i = 0; i < taxRules.length; i++) {
            taxValue = taxValue*1 + taxRules[i].taxLegVariance.txlvRate*1;
        }
        return taxValue;
    }

    function getProfitMargin () {
        if ($scope.customer == undefined || $scope.customer==null) {
            return 0.10;
        }
        return $scope.customer.grade.rate;
    }

    $scope.addTxnMedia= function() {
        var rowId;
        if ($scope.txnMediaList.data == undefined && $scope.txnMediaList.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.txnMediaList.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        txnMedia = {
            "id" : rowId,
            "paymentMedia":$scope.paymentMedia,
            "txmdAmountLocal" : $scope.paymentAmount
        }
        $scope.txnMediaList.data.push(txnMedia);
        totalTransaction();
    };
    $scope.removeTxnMedia= function () {
        var selectedRow = $scope.selectedTxnMediaRow;
        rowIndex = getArrIndexOf($scope.txnMediaList.data, selectedRow);
        if (rowIndex>-1) {
            $scope.txnMediaList.data.splice(rowIndex,1);
        }
        totalTransaction();
    };
    function getArrIndexOf(arr,item) {
        if (arr == undefined || item== undefined)
            return -1;
        for (var j = 0; j < arr.length; j++) {
            if (arr[j].id == item.id) {
                return j;
            }
        }
        return -1;
    };

    $scope.createTransactionSale = function () {


        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;
        $scope.txnHeaderForm.customer = $scope.customer;
        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                if ($scope.isPageNew && $scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_QUOTE' ) {
                    baseDataService.displayMessage("info","Quote Number", "Quote saved with number: " + addResponse.info);
                }
                if ($scope.isPageNew && $scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_SALE' ) {
                    baseDataService.displayMessage("info","Txn Number", "Sale transaction saved with number: " + addResponse.info);
                }
                if (!$scope.isPageNew && $scope.txnHeaderForm.convertedToTxnSale) {
                    baseDataService.displayMessage("info","Txn Number", "Sale transaction saved with number: " + addResponse.info);
                }
                $state.go('dashboard.listSaleTransaction');
            } else {
                alert('Not able to save Transaction. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

    function totalTransaction() {
        var txnDetailList =  $scope.txnDetailList.data;
        var valueGross = 0.00;
        var valueNett = 0.00;
        var valueTax = 0.00;
        for (var i = 0; i < txnDetailList.length; i++) {
            if (!txnDetailList[i].txdeItemVoid && !txnDetailList[i].deleted ) {
                valueNett = valueNett*1 + txnDetailList[i].txdeValueNet*txnDetailList[i].txdeQuantitySold*1 ;
                valueGross = valueGross*1 + txnDetailList[i].txdeValueGross*txnDetailList[i].txdeQuantitySold*1;
                valueTax = valueTax*1 + txnDetailList[i].txdeTax*txnDetailList[i].txdeValueLine*txnDetailList[i].txdeQuantitySold*1;
            }
        }
        $scope.txnHeaderForm.txhdValueNett = valueNett;
        $scope.txnHeaderForm.txhdValueGross = valueGross;
        $scope.txnHeaderForm.txhdValueTax = valueTax;
        $scope.txnHeaderForm.txhdValueDue = valueNett - $scope.calculateAmountPaid();
    }
    $scope.calculateAmountPaid = function() {
        if ($scope.txnMediaList == undefined || $scope.txnMediaList.data == undefined) {
            return 0.00;
        }
        var txnMediaList =  $scope.txnMediaList.data;
        var total = 0.00;
        for (var i = 0; i < txnMediaList.length; i++) {
            if (!txnMediaList[i].txmdVoided) {
                total = total*1 + txnMediaList[i].txmdAmountLocal*1;
            }
        }
        return total;
    }

    $scope.voidItem = function(row) {
        if (!confirm('Are you sure you want to void this item?')) {
            return;
        }
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        row.entity.txdeItemVoid = true;
        totalTransaction();
    };

    $scope.voidTender = function(row) {
        if (!confirm('Are you sure you want to void this tender?')) {
            return;
        }
        if (row == undefined || row.entity == undefined) {
            alert('tender is undefined');
            return;
        }
        row.entity.txmdVoided = true;
        totalTransaction();
    };

    /** delete Txn Detail **/
    $scope.removeItem = function(row) {
        if (!confirm('Are you sure you want to delete this item?')) {
            return;
        }
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }

        row.entity.deleted = true;
        $scope.txnDetailGridApi.core.setRowInvisible(row);
        totalTransaction();
    }
    $scope.createTransactionSaleDraft = function() {
        baseDataService.getBaseData(TXN_STATE_DRAFT).then(function(response){
            $scope.txnHeaderForm.txhdState = response.data;
            $scope.createTransactionSale();
        });
    }

    $scope.submitTransaction = function() {
        baseDataService.getBaseData(TXN_STATE_FINAL).then(function(response){
            $scope.txnHeaderForm.txhdState = response.data;
            $scope.createTransactionSale();
        });
    }

    $scope.convertToSaleTxn = function() {
        //when converting to txn_sale, we need to change the state to draft.
        if ($scope.txnHeaderForm.convertedToTxnSale) {
            baseDataService.getBaseData(TXN_TYPE_SALE).then(function(response){
                $scope.txnHeaderForm.txhdTxnType = response.data;
            });
            baseDataService.getBaseData(TXN_STATE_DRAFT).then(function(response){
                $scope.txnHeaderForm.txhdState = response.data;
            });
        } else {
            baseDataService.getBaseData(TXN_TYPE_QUOTE).then(function(response){
                $scope.txnHeaderForm.txhdTxnType = response.data;
            });
        }
    }

    $scope.isTxnLineVoidable = function () {
        console.log('txn_state_code = ' + $scope.txnHeaderForm.txhdState.categoryCode);
        if ($scope.txnHeaderForm.txhdState.categoryCode != 'TXN_STATE_FINAL') {
            return true;
        }
        return false;
    }
});
