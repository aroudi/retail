/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnRefundCtrl', function($scope, $state, $timeout, $stateParams, baseDataService,ngDialog, uiGridConstants, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI, TXN_REFUND_URI, TXN_MEDIA_REFUND, INVOICE_EXPORT_PDF) {


    //select all rows for refund


    $scope.refundMode = false;
    initPageData();
    initTxnDetail();
    initTxnMediaList();

    function initPageData() {
        baseDataService.getBaseData(TXN_MEDIA_REFUND).then(function(response){
            $scope.txnMediaTypeRefund = response.data;
        });

        $scope.txnHeaderForm = angular.copy(baseDataService.getRow());
        //keep a copy of paid media list for calculation.
        $scope.paidMediaList = angular.copy($scope.txnHeaderForm.txnMediaFormList);
        //set new paid media list
        //$scope.txnHeaderForm.txnMediaFormList = [];
        //select all rows for refund
        baseDataService.setRow({});
        baseDataService.setIsPageNew(true);
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
                {field: 'product.prodSku', displayName: 'SKU', enableCellEdit: false, width: '13%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodSku
                    }
                },
                {field: 'product.prodName', displayName: 'Name', enableCellEdit: false, width: '18%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodName
                    }
                },
                {field: 'unitOfMeasure.unomDesc', displayName: 'Size', enableCellEdit: false, width: '7%'},
                {field: 'txdeValueGross', displayName: 'Price', cellFilter: 'currency', width: '10%'},
                {field: 'txidSurcharge', displayName: 'Surcharge', cellFilter: 'number', width: '7%'},
                {field: 'txdeQtyTotalInvoiced', displayName: 'Qty Invoiced', enableCellEdit: false, type: 'number', width: '8%'},
                {field: 'txdeQtyRefund', displayName: 'Qty Refund', type: 'number', width: '7%'},
                {field: 'txdeQtyBalance', displayName: 'Balance',enableCellEdit: false, type: 'number', width: '7%'},
                {field: 'calculatedLineValue', displayName: 'Aomount', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'calculatedLineTax', displayName: 'Tax',cellFilter: 'currency', footerCellFilter: 'currency', enableCellEdit: false, width: '7%'},
                {field: 'txdePriceSold', displayName: 'Total', cellFilter: 'currency', footerCellFilter: 'currency', enableCellEdit: false, width: '8%'}
            ]
        }
        $scope.txnDetailList.enableRowSelection = true;
        $scope.txnDetailList.multiSelect = true;
        $scope.txnDetailList.enableSelectAll = false;

        //init data
        $scope.txnDetailList.data = angular.copy($scope.txnHeaderForm.txnDetailFormList);

        //$scope.txnDetailList.noUnselect = true;
        //
        $scope.txnDetailList.onRegisterApi = function (gridApi) {
            $scope.txnDetailGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                //$scope.selectedTxnDetailRow = row.entity;
                //check if it had been invoiced totally before, prevent user from selecting the row
                if ((row.entity.txdeQtyTotalInvoiced*1<=0) || (row.entity.txdeQtyTotalRefund*1  >= row.entity.txdeQtyTotalInvoiced*1)) {
                    gridApi.selection.unSelectRow(row.entity);
                    return;
                }
                if (row.isSelected) {
                    row.entity.refund = true;
                } else {
                    row.entity.refund = false;
                    row.entity.txdeQtyRefund = 0;
                }
                checkIfItemSelected();
                evaluatRowItem(row.entity);
                totalTransaction();
            });
            gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
                if (colDef.name == 'txdeQtyRefund') {
                    $scope.txdeQtyRefundBeforeEditting = rowEntity.txdeQtyRefund;
                }
            })
        };

        $scope.$on('uiGridEventEndCellEdit', function (event) {
            var txnDetail = event.targetScope.row.entity;
            if ( event.targetScope.col.field == 'txdeQtyRefund' && (txnDetail.refund==false)) {
                txnDetail['txdeQtyRefund'] = $scope.txdeQtyRefundBeforeEditting;
                txnDetail['txdeQtyBalance'] = txnDetail.txdeQtyTotalInvoiced*1 - (txnDetail.txdeQtyTotalRefund*1 + txnDetail.txdeQtyRefund*1);
                return;
            }

            //if it has not been invoiced before and total invoiced is undefined:
            if (txnDetail['txdeQtyTotalRefund'] == undefined) {
                txnDetail['txdeQtyTotalRefund'] = 0;
            }
            var newBalance = txnDetail.txdeQtyTotalInvoiced*1 - (txnDetail.txdeQtyTotalRefund*1 + txnDetail.txdeQtyRefund*1);
            if (newBalance < 0) {
                baseDataService.displayMessage('info','Invalid Qty', 'Refund Quantity is higher than total invoiced Quantity !!!');
                if (event.targetScope.col.field == 'txdeQtyRefund') {
                    //txnDetail.txdeQtyInvoice = $scope.txdeQtyInvoicedBeforeEditting;
                    txnDetail[event.targetScope.col.field] = $scope.txdeQtyRefundBeforeEditting;
                }
                return;
            }
            //check the mode.
            var quantity = (-1)*txnDetail.txdeQtyRefund;
            txnDetail['txdeQtyBalance'] = newBalance;
            //txnDetail['txdeValueNet'] =  (txnDetail.txdeValueGross * txnDetail.txdeTax)*1 + txnDetail.txdeValueGross*1;
            txnDetail['txdePriceSold'] = quantity * (txnDetail.txdeValueNet - calculateItemSurcharge(txnDetail));
            txnDetail['calculatedLineValue'] = txnDetail.txdeValueGross * quantity;
            txnDetail['calculatedLineTax'] = txnDetail.calculatedLineValue * txnDetail.txdeTax;
            totalTransaction();
        })
        //$scope.allItemsSelected=false;
    }


    function initTxnMediaList() {
        var tenderTpl='<div ng-class="{\'blue\':row.entity.txmdVoided==false, \'red\':row.entity.txmdVoided==true }"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
        $scope.txnMediaList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter:true,
            gridFooterTemplate:"<div id=\"currency-default\"> Total:{{grid.appScope.getAmountPaid() | currency}}</div>",
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
            /*
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
            */
        };
        $scope.txnMediaList.data =$scope.txnHeaderForm.txnMediaFormList;
    }

    function evaluatRowItem (txnDetail) {
        var quantity = 0;
        if ($scope.refundMode) {
            txnDetail.txdeQtyBalance = txnDetail.txdeQtyTotalInvoiced*1 - (txnDetail.txdeQtyTotalRefund*1 + txnDetail.txdeQtyRefund*1);
            quantity = (-1)*txnDetail.txdeQtyRefund;
        } else {
            txnDetail.txdeQtyBalance = txnDetail.txdeQtyTotalInvoiced*1 - txnDetail.txdeQtyTotalRefund*1;
            quantity = txnDetail.txdeQtyTotalInvoiced*1;
        }
        txnDetail.txdePriceSold =  quantity * (txnDetail.txdeValueNet - calculateItemSurcharge(txnDetail)*1);
        txnDetail.calculatedLineValue = txnDetail.txdeValueGross * quantity;
        txnDetail.calculatedLineTax = txnDetail.calculatedLineValue * txnDetail.txdeTax;

    }


    $scope.addTxnMedia= function() {
        if (!$scope.refundMode) {
            baseDataService.displayMessage('info','Not Allowed', 'please select an item for refund');
            return;
        }
        var rowId;
        if ($scope.txnMediaList.data == undefined && $scope.txnMediaList.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.txnMediaList.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var txnMediaType = $scope.txnMediaTypeRefund;
        //check the remain on account. if user selects account media, we need to take credit back from customer.
        //if customer dosn't owe us, we can not refund to account tender.
        var refundAmount=0.00;
        var accountBalance = $scope.accountBalance();
        if ($scope.paymentMedia.paymName === 'Account') {
            if ( accountBalance <=0.00) {
                baseDataService.displayMessage('info','Not Allowed', 'You are not allowed to refund to Client`s Account !!!');
                return;
            }
            if (Math.abs($scope.paymentAmount) >= accountBalance ) {
                refundAmount = (1)*accountBalance;
            } else {
                refundAmount =$scope.paymentAmount;
            }
        } else {
            refundAmount =$scope.paymentAmount;
        }
        txnMedia = {
            "id" : rowId,
            "paymentMedia":$scope.paymentMedia,
            "txmdAmountLocal" : (-1)*refundAmount,
            "txmdVoided":false,
            "deleted" : false,
            "txmdType": txnMediaType
        }
        $scope.txnMediaList.data.push(txnMedia);
        totalTransaction();
        //check if transaction is fully paid
        if ($scope.txnHeaderForm.txhdValueDue == 0) {
            baseDataService.displayMessage('yesNo','Sumbit!!','Do you want to Submit Transaction?').then(function(result){
                if (result) {
                    $scope.createTransactionRefund();
                } else {
                    return;
                }
            });
        }
    };
    $scope.removeTxnMedia= function (row) {
        if (!$scope.refundMode) {
            baseDataService.displayMessage('info','Not Allowed', 'please select an item for refund');
            return;
        }
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

    $scope.createTransactionRefund = function () {

        //check if we have outstanding amount to pay
        if (maxPaymentAllowed() > 0) {
            baseDataService.displayMessage("info","Warning", "Payment is due!!!");
            return;
        }

        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;

        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_REFUND_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $scope.txnHeaderForm.id=addResponse.info;
                $scope.exportToPdf(INVOICE_EXPORT_PDF);
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

    function calculateAmountPaid(){
        if ($scope.txnMediaList == undefined || $scope.txnMediaList.data == undefined) {
            return 0.00;
        }
        var txnMediaList =  $scope.txnMediaList.data;
        var total = 0.00;
        for (var i = 0; i < txnMediaList.length; i++) {
            if (!txnMediaList[i].txmdVoided && !txnMediaList[i].deleted) {
                total = total*1 + txnMediaList[i].txmdAmountLocal*1;
            }
        }
        return total;
    }

    function totalTransaction() {
        var txnDetailList =  $scope.txnDetailList.data;
        var valueGross = 0.00;
        var valueNett = 0.00;
        var valueTax = 0.00;
        var quantity = 0;


        for (var i = 0; i < txnDetailList.length; i++) {

            if($scope.refundMode) {
                if (!txnDetailList[i].refund) {
                    continue;
                }
                quantity = (-1)*txnDetailList[i].txdeQtyRefund;
            }else {
                quantity = txnDetailList[i].txdeQtyTotalInvoiced*1;
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
        $scope.txnHeaderForm.txhdValueDue = valueNett - calculateAmountPaid();
        //check if amount due is less than 5 cents, then add it to rounding
        console.log('Math.abs($scope.txnHeaderForm.txhdValueDue) =' + Math.abs($scope.txnHeaderForm.txhdValueDue));
        if (Math.abs($scope.txnHeaderForm.txhdValueDue) <= 0.05 ) {
            console.log('set rounding value');
            $scope.txnHeaderForm.txhdValRounding = $scope.txnHeaderForm.txhdValueDue;
            $scope.txnHeaderForm.txhdValueDue = 0.00;
        }
        //set default value of payment amount to value due.
        $scope.paymentAmount = maxPaymentAllowed()*1;

    }


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


    function maxPaymentAllowed() {
        var maxPayment = (Math.round($scope.txnHeaderForm.txhdValueDue*Math.pow(10,1))/Math.pow(10,1)).toFixed(2);
        console.log("max payment = " + maxPayment);
        return Math.abs(maxPayment);
    }

    $scope.maxPaymentAllowed = function() {
        return maxPaymentAllowed();
    }

    function amountPaidFromAccount() {
        var amountPaidFromAcount = 0.00;
        if ($scope.paidMediaList == undefined || $scope.paidMediaList.length ===0) {
            return amountPaidFromAcount;
        }
        $scope.paidMediaList.forEach(function (txnMedia){
            if ((!txnMedia.txmdVoided) && (txnMedia.paymentMedia.paymName === 'Account')) {
                amountPaidFromAcount = amountPaidFromAcount*1 + txnMedia.txmdAmountLocal*1;
            }
        })
        console.log('amountPaidFromAcount = ' + amountPaidFromAcount);
        return amountPaidFromAcount;
    }
    function amountAllreadyPaidByAccount() {
        var amountPaidbyAcount = 0.00;
        if ($scope.txnMediaList.data == undefined || $scope.txnMediaList.data.length ===0) {
            return amountPaidbyAcount;
        }
        $scope.txnMediaList.data.forEach(function (txnMedia){
            if ((!txnMedia.txmdVoided) && (txnMedia.paymentMedia.paymName === 'Account')) {
                amountPaidbyAcount = amountPaidbyAcount*1 + txnMedia.txmdAmountLocal*1;
            }
        })
        console.log('amountPaidbyAcount = ' + amountPaidbyAcount);
        return amountPaidbyAcount;
    }
    $scope.accountBalance = function() {
        var balance = amountPaidFromAccount()*1 - amountAllreadyPaidByAccount()*(-1);
        console.log('account balance = ' + balance);
        return balance;
    }


    $scope.exportToPdf = function(url) {
        var exportUrl = url + $scope.txnHeaderForm.id;
        baseDataService.pdfViewer(exportUrl);
        $state.go('dashboard.listInvoice');
    }
    function selectAllRowsForRefund() {
        $scope.refundMode = true;
        if ($scope.txnDetailList === undefined) {
            return;
        }
        //$scope.txnDetailGridApi.selection.selectAllRows();
        for (var i = 0; i < $scope.txnDetailList.data.length; i++) {
            console.log('$scope.txnDetailList.data[i].txdeQtyBalance = ' + $scope.txnDetailList.data[i].txdeQtyBalance);
            //we can refund only invoiced items.
            if ($scope.txnDetailList.data[i].txdeQtyTotalInvoiced <= 0) {
                continue;
            }
            if ($scope.txnDetailList.data[i].txdeQtyBalance > 0) {
                $scope.txnDetailGridApi.selection.selectRow($scope.txnDetailList.data[i]);
                $scope.txnDetailList.data[i].refund = true;
                $scope.txnDetailList.data[i].txdeQtyRefund = $scope.txnDetailList.data[i].txdeQtyBalance;
                //evaluatRowItem($scope.txnDetailList.data[i]);
            }
        }
        //totalTransaction();
        $scope.allItemsSelected=true;
        togglePageMode();
    }
    function unSelectAllRows() {
        $scope.refundMode = false;
        if ($scope.txnDetailList === undefined) {
            return;
        }
        $scope.txnDetailGridApi.selection.clearSelectedRows();
        for (var i = 0; i < $scope.txnDetailList.data.length; i++) {
            $scope.txnDetailList.data[i].refund = false;
            $scope.txnDetailList.data[i].txdeQtyRefund = 0;
            //evaluatRowItem($scope.txnDetailList.data[i]);
        }
        //totalTransaction();
        $scope.allItemsSelected=false;
        togglePageMode();
    }
    $scope.toggleAllRowSelection = function() {
        if ($scope.allItemsSelected) {
            unSelectAllRows();
        } else {
            selectAllRowsForRefund();
        }
    }

    $scope.getAmountPaid = function () {
        return calculateAmountPaid();
    }

    function checkIfItemSelected(){
        var refundModeBeforeSelection = $scope.refundMode;
        if ($scope.txnDetailGridApi.selection.getSelectedRows().length > 0) {
            $scope.refundMode = true;
        } else {
            $scope.refundMode = false;
        }
        if (refundModeBeforeSelection != $scope.refundMode) {
            togglePageMode();
        }
    }

    function togglePageMode() {
        // change to sale mode
        if ($scope.refundMode) {
            //revert back media list to paid list.
            $scope.txnMediaList.data = [];
        } else {
            $scope.txnMediaList.data = angular.copy($scope.paidMediaList);
        }
        for (var i = 0; i < $scope.txnDetailList.data.length; i++) {
            evaluatRowItem($scope.txnDetailList.data[i]);
        }
        totalTransaction();
    }

    function calculateItemSurcharge(txnDetail) {
        if (txnDetail == undefined) {
            return 0.00;
        }
        if (txnDetail.txidSurcharge == undefined) {
            return 0.00;
        }
        if ($scope.refundMode) {
            return (txnDetail.txidSurcharge * txnDetail.txdeValueGross)/100;
        }
        return 0.00;
    }

    $scope.enableRefundButton = function() {
        return $scope.refundMode && ($scope.txnHeaderForm.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE');
    }
});
