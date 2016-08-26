/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleCtrl', function($scope, $state, $timeout, $stateParams, baseDataService,ngDialog, uiGridConstants, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI, TXN_ADD_URI, TXN_TYPE_QUOTE, TXN_TYPE_SALE, TXN_STATE_FINAL, TXN_STATE_DRAFT, TXN_EXPORT_PDF, TXN_ADD_PAYMENT_URI, TXN_INVOICE_URI, TXN_MEDIA_SALE, TXN_MEDIA_DEPOSIT) {

    $scope.isPageNew = baseDataService.getIsPageNew();
    /*
          *  by default it is a sale order until user select invoice option on the page.
          *  this variable indicates display mode which are InvoiceMode and SaleOrderMode
     */
    $scope.isInvoiceMode = false;
    initPageData();
    initTxnDetail();
    initTxnMediaList();

    function initPageData() {
        baseDataService.getBaseData(TXN_MEDIA_SALE).then(function(response){
            $scope.txnMediaTypeSale = response.data;
        });
        baseDataService.getBaseData(TXN_MEDIA_DEPOSIT).then(function(response){
            $scope.txnMediaTypeDeposit = response.data;
        });
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
            enableRowSelection:true,
            gridFooterTemplate:"<div id=\"currency-default\"> Total:{{grid.appScope.txnHeaderForm.txhdValueNett | currency}}</div>",
            rowTemplate : rowtpl,
            columnDefs: [
                {field: 'id', visible: false, enableCellEdit: false},
                {field: 'product.prodSku', displayName: 'SKU', enableCellEdit: false, width: '10%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodSku
                    }
                },
                {field: 'product.prodName', displayName: 'Name', enableCellEdit: false, width: '20%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodName
                    }
                },
                {field: 'unitOfMeasure.unomDesc', displayName: 'Size', enableCellEdit: false, width: '5%'},
                //{field: 'txdeValueLine', displayName: 'Cost', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                //{field: 'txdeValueProfit', displayName: 'Profit', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'txdeValueGross', displayName: 'Price', cellFilter: 'currency', width: '10%'},
                {field: 'txdeQuantitySold', displayName: 'Qty', type: 'number', width: '7%'},
                {field: 'txdeQtyInvoiced', displayName: 'Invoice', type: 'number', width: '7%'},
                {field: 'txdeQtyBalance', displayName: 'Balance', type: 'number', width: '7%'},
                {field: 'calculatedLineValue', displayName: 'Aomount', enableCellEdit: false, cellFilter: 'currency', width: '9%'},
                {field: 'calculatedLineTax', displayName: 'Tax', enableCellEdit: false, width: '7%'},
                {field: 'txdePriceSold', displayName: 'Total', cellFilter: 'currency', footerCellFilter: 'currency', enableCellEdit: false, width: '10%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Item" ng-show="grid.appScope.isTxnLineVoidable(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidItem(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" ng-show="row.entity.id < 0" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a>', width: '8%'}

            ]
        }
        $scope.txnDetailList.enableRowSelection = true;
        $scope.txnDetailList.multiSelect = true;
        //$scope.txnDetailList.noUnselect = true;
        //
        $scope.txnDetailList.onRegisterApi = function (gridApi) {
            $scope.txnDetailGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                //$scope.selectedTxnDetailRow = row.entity;
                if (row.isSelected) {
                    row.entity.invoiced = true;
                } else {
                    row.entity.invoiced = false;
                    row.entity.txdeQtyInvoiced = 0;
                }
                checkIfItemSelected();
                evaluatRowItem(row.entity);
                totalTransaction();
            });
            gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
                console.log('beginCellEdit');
                if (colDef.name == 'txdeQuantitySold') {
                    $scope.txdeQuantitySoldBeforeEditting = rowEntity.txdeQuantitySold;
                }
                if (colDef.name == 'txdeQtyInvoiced') {
                    $scope.txdeQtyInvoicedBeforeEditting = rowEntity.txdeQtyInvoiced;
                }
            })
        };

        $scope.$on('uiGridEventEndCellEdit', function (event) {
            var txnDetail = event.targetScope.row.entity;
            if ( event.targetScope.col.field == 'txdeQtyInvoiced' && (txnDetail.invoiced==false)) {
                console.log('set invoice back to original : ' + $scope.txdeQtyInvoicedBeforeEditting);
                txnDetail.txdeQtyInvoice = $scope.txdeQtyInvoicedBeforeEditting;
                txnDetail['txdeQtyInvoiced'] = $scope.txdeQtyInvoicedBeforeEditting;
                txnDetail.txdeQtyBalance = txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1);
                txnDetail['txdeQtyBalance'] = txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1);
                return;
            }
            //cellData = txnDetail[event.targetScope.col.field];
            //if it has not been invoiced before and total invoiced is undefined:
            if (txnDetail.txdeQtyTotalInvoiced == undefined) {
                txnDetail.txdeQtyTotalInvoiced = 0;
            }
            var newBalance = txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1);
            if (newBalance < 0) {
                baseDataService.displayMessage('info','Invalid Qty', 'Invoiced Quantity is higher than total Quantity !!!');
                if (event.targetScope.col.field == 'txdeQuantitySold') {
                    txnDetail.txdeQuantitySold = $scope.txdeQuantitySoldBeforeEditting;
                    txnDetail[event.targetScope.col.field] = $scope.txdeQuantitySoldBeforeEditting;
                }
                if (event.targetScope.col.field == 'txdeQtyInvoiced') {
                    txnDetail.txdeQtyInvoice = $scope.txdeQtyInvoicedBeforeEditting;
                    txnDetail[event.targetScope.col.field] = $scope.txdeQtyInvoicedBeforeEditting;
                }
                return;
            }
            //check the mode.
            var quantity = 0;
            if ($scope.isInvoiceMode) {
                quantity = txnDetail.txdeQtyInvoiced;
            } else {
                quantity = txnDetail.txdeQuantitySold;
            }
            txnDetail.txdeQtyBalance = newBalance;
            txnDetail.txdeValueNet =  (txnDetail.txdeValueGross * txnDetail.txdeTax)*1 + txnDetail.txdeValueGross*1;
            txnDetail.txdePriceSold = quantity * txnDetail.txdeValueNet;
            txnDetail.calculatedLineValue = txnDetail.txdeValueGross * quantity;
            txnDetail.calculatedLineTax = txnDetail.calculatedLineValue * txnDetail.txdeTax;
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
                {field:'txmdType.displayName',displayName:'Type', visible:true, enableCellEdit:false, width: '10%'},
                {field:'paymentMedia.paymName',displayName:'Payment Media', visible:true, enableCellEdit:false, width: '45%',
                    cellTooltip: function(row,col) {
                        return row.entity.paymentMedia.paymName
                    }
                },
                {field:'txmdAmountLocal', displayName:'Amount', visible:true, cellFilter:'currency', footerCellFilter:'currency', /*aggregationType: uiGridConstants.aggregationTypes.sum, */ enableCellEdit:false, width: '35%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Tender" ng-show="grid.appScope.isTxnLineVoidable(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidTender(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" ng-show="row.entity.id < 0" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeTxnMedia(row)"></i></a>', width: '10%'}

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
                        txnDetail.txdeQtyTotalInvoiced =  0;
                        txnDetail.txdeQuantitySold =  1;
                        txnDetail.txdeQtyInvoiced =  0;
                        txnDetail.txdeQtyBalance =  txnDetail.txdeQuantitySold;
                        evaluatRowItem(txnDetail);
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
            'deleted' : false,
            'invoiced' : false

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
        txnDetail.txdeQtyBalance = txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1)

        var quantity = 0;
        if ($scope.isInvoiceMode) {
            quantity = txnDetail.txdeQtyInvoiced;
        } else {
            quantity = txnDetail.txdeQuantitySold;
        }

        txnDetail.txdePriceSold =  quantity * txnDetail.txdeValueNet;
        txnDetail.calculatedLineValue = txnDetail.txdeValueGross * quantity;
        txnDetail.calculatedLineTax = txnDetail.calculatedLineValue * txnDetail.txdeTax;

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
        if ($scope.customer == undefined || $scope.customer==null || $scope.customer.grade == undefined) {
            return 0.20;
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
        var txnMediaType;
        if ($scope.isInvoiceMode) {
            txnMediaType = $scope.txnMediaTypeSale;
        }else {
            txnMediaType = $scope.txnMediaTypeDeposit;
        }
        txnMedia = {
            "id" : rowId,
            "paymentMedia":$scope.paymentMedia,
            "txmdAmountLocal" : $scope.paymentAmount,
            "txmdVoided":false,
            "deleted" : false,
            "txmdType": txnMediaType
        }
        $scope.txnMediaList.data.push(txnMedia);
        totalTransaction();
    };
    $scope.removeTxnMedia= function (row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item??').then(function(result){
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    baseDataService.displayMessage('info','Warning!!','item is undefined');
                    return;
                }
                row.entity.deleted = true;
                $scope.txnMediaGridApi.core.setRowInvisible(row);
                totalTransaction();
            } else {
                return;
            }
        });

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
        var quantity = 0;
        for (var i = 0; i < txnDetailList.length; i++) {
            if ($scope.isInvoiceMode) {
                //check if row has been selected for invoice or not!!!!
                if (!txnDetailList[i].invoiced) {
                    continue;
                }
                quantity = txnDetailList[i].txdeQtyInvoiced;
            } else {
                quantity = txnDetailList[i].txdeQuantitySold;
            }

            if (!txnDetailList[i].txdeItemVoid && !txnDetailList[i].deleted ) {
                valueNett = valueNett*1 + txnDetailList[i].txdeValueNet*quantity*1 ;
                valueGross = valueGross*1 + txnDetailList[i].txdeValueGross*quantity*1;
                valueTax = valueTax*1 + txnDetailList[i].txdeTax*txnDetailList[i].txdeValueLine*quantity*1;
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
            if ($scope.isInvoiceMode) {
                //in invoice mode we only need to calculate new added rows + deposit paid, because the rest are belong to the transaction
                if ( !(txnMediaList[i].txmdVoided || txnMediaList[i].deleted || txnMediaList[i].id > 0) || ((!txnMediaList[i].txmdVoided) && (txnMediaList[i].txmdType.categoryCode == 'TXN_MEDIA_DEPOSIT'))) {
                    total = total*1 + txnMediaList[i].txmdAmountLocal*1;
                }

            } else {
                if (!txnMediaList[i].txmdVoided && !txnMediaList[i].deleted) {
                    total = total*1 + txnMediaList[i].txmdAmountLocal*1;
                }
            }
        }
        return total;
    }

    $scope.voidItem = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to void this item??').then(function(result){
            console.log('result :', result);
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    alert('item is undefined');
                    return;
                }
                row.entity.txdeItemVoid = true;
                totalTransaction();
            } else {
                return;
            }
        });
    };

    $scope.voidTender = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to void this tender?').then(function(result){
            console.log('result :', result);
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    alert('tender is undefined');
                    return;
                }
                row.entity.txmdVoided = true;
                totalTransaction();
            } else {
                return;
            }
        });

    };

    /** delete Txn Detail **/
    $scope.removeItem = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            console.log('result :', result);
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    alert('item is undefined');
                    return;
                }

                row.entity.deleted = true;
                $scope.txnDetailGridApi.core.setRowInvisible(row);
                totalTransaction();
            } else {
                return;
            }
        });
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

    $scope.isTxnLineVoidable = function (row) {
        if ( ($scope.txnHeaderForm.txhdState.categoryCode != 'TXN_STATE_FINAL') && (row.entity.id > 0)) {
            return true;
        }
        return false;
    }
    $scope.isTxnSaleAndFinal = function () {
        if ($scope.txnHeaderForm.txhdState.categoryCode == 'TXN_STATE_FINAL' && $scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_SALE') {
            return true;
        }
        return false;
    }
    $scope.exportToPdf = function() {

        var exportUrl = TXN_EXPORT_PDF + $scope.txnHeaderForm.id;
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
    }

    $scope.addPayment = function () {

        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;
        $scope.txnHeaderForm.customer = $scope.customer;
        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_ADD_PAYMENT_URI).then(function(response) {
            var addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listSaleTransaction');
            } else {
                alert('Not able to save Transaction. ' + addResponse.message);
            }
        });
        return;
    }

    /**
     *
     * @param mode true:invoice false:saleOrder
     */
    function changeToInvoiceMode() {
        var mode = $scope.isInvoiceMode;

        //recalculate txnDetail rows.
        $scope.txnDetailList.data.forEach(function (row){
            evaluatRowItem(row);
        })

        if (mode) {
            //copy existing medias
            var invoiceMediaList= [];
            $scope.txnMediaListBackup = angular.copy($scope.txnMediaList.data);
            $scope.txnMediaList.data.forEach(function (row){
                //we need to display new added media and also deposit as well.
                if (row.id < 0 || row.txmdType.categoryCode == 'TXN_MEDIA_DEPOSIT') {
                    invoiceMediaList.push(row);
                }
            })
            $scope.txnMediaList.data = invoiceMediaList;
        } else {
            //copy new added rows to the backup one.
            $scope.txnMediaList.data.forEach(function (row){
                //for new added rows
                if ( !(row.id > 0 || row.deleted)) {
                    //if row not exists in backup then add it
                    if (baseDataService.getArrIndexOf($scope.txnMediaListBackup, row)<0) {
                        $scope.txnMediaListBackup.push(row);
                    }
                }
            })
            //now set the data to backup
            $scope.txnMediaList.data = $scope.txnMediaListBackup;
        }
        totalTransaction();
    }
    $scope.invoiceTransactionSale = function () {
        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;

        $scope.txnHeaderForm.customer = $scope.customer;
        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_INVOICE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                baseDataService.displayMessage("info","Invoice Number", "Invoice saved with number: " + addResponse.info);
                $state.go('dashboard.listSaleTransaction');
            } else {
                alert('Not able to save Transaction. ' + addResponse.message);
            }
        });
        return;
    }

    function checkIfItemSelected(){
        var invoiceModeBeforeSelection = $scope.isInvoiceMode;
        if ($scope.txnDetailGridApi.selection.getSelectedRows().length > 0) {
            $scope.isInvoiceMode = true;
        } else {
            $scope.isInvoiceMode = false;
        }
        if (invoiceModeBeforeSelection != $scope.isInvoiceMode) {
            changeToInvoiceMode();
        }
    }


});
