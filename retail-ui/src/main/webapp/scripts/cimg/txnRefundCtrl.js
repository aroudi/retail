/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnRefundCtrl', function($scope, $state, $timeout, $stateParams, baseDataService,ngDialog, uiGridConstants, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI, TXN_ADD_URI, TXN_MEDIA_REFUND) {

    initPageData();
    initTxnDetail();
    initTxnMediaList();

    function initPageData() {
        baseDataService.getBaseData(TXN_MEDIA_REFUND).then(function(response){
            $scope.txnMediaTypeRefund = response.data;
        });

        $scope.txnHeaderForm = angular.copy(baseDataService.getRow());
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
                {field: 'txdeQtyTotalInvoiced', displayName: 'Qty', enableCellEdit: false, type: 'number', width: '7%'},
                {field: 'txdeQtyRefund', displayName: 'Refund', type: 'number', width: '7%'},
                {field: 'txdeQtyBalance', displayName: 'Balance',enableCellEdit: false, type: 'number', width: '7%'},
                {field: 'calculatedLineValue', displayName: 'Aomount', enableCellEdit: false, cellFilter: 'currency', width: '9%'},
                {field: 'calculatedLineTax', displayName: 'Tax', enableCellEdit: false, width: '7%'},
                {field: 'txdePriceSold', displayName: 'Total', cellFilter: 'currency', footerCellFilter: 'currency', enableCellEdit: false, width: '10%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Item" ng-show="grid.appScope.isTxnLineVoidable(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidItem(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" ng-show="row.entity.id < 0" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a>', width: '8%'}

            ]
        }
        $scope.txnDetailList.enableRowSelection = true;
        $scope.txnDetailList.multiSelect = true;

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
                    $scope.txdeQtyRefundBeforeEditting = rowEntity.txdeRefund;
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
            txnDetail['txdePriceSold'] = quantity * txnDetail.txdeValueNet;
            txnDetail['calculatedLineValue'] = txnDetail.txdeValueGross * quantity;
            txnDetail['calculatedLineTax'] = txnDetail.calculatedLineValue * txnDetail.txdeTax;
            totalTransaction();
        })
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

        //keep a copy of paid media list for calculation.
        $scope.paidMediaList = angular.copy($scope.txnHeaderForm.txnMediaFormList);
        //set new paid media list
        $scope.txnHeaderForm.txnMediaFormList = [];
    }

    function evaluatRowItem (txnDetail) {
        txnDetail.txdeQtyBalance = txnDetail.txdeQtyTotalInvoiced*1 - (txnDetail.txdeQtyTotalRefund*1 + txnDetail.txdeQtyRefund*1);

        var quantity = (-1)*txnDetail.txdeQtyRefund;

        txnDetail.txdePriceSold =  quantity * txnDetail.txdeValueNet;
        txnDetail.calculatedLineValue = txnDetail.txdeValueGross * quantity;
        txnDetail.calculatedLineTax = txnDetail.calculatedLineValue * txnDetail.txdeTax;

    }


    $scope.addTxnMedia= function() {
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
                refundAmount = (-1)*accountBalance;
            } else {
                refundAmount =$scope.paymentAmount;
            }
        } else {
            refundAmount =$scope.paymentAmount;
        }
        txnMedia = {
            "id" : rowId,
            "paymentMedia":$scope.paymentMedia,
            "txmdAmountLocal" : refundAmount,
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

    $scope.createTransactionRefund = function () {


        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;

        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
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
            if (!txnDetailList[i].refund) {
                continue;
            }
            quantity = (-1)*txnDetailList[i].txdeQtyRefund;

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
    $scope.calculateAmountPaid = function() {
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
        return maxPayment;
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
                amountPaidFromAcount = amountPaidFromAcount*1 + txnMedia.paymentMedia.txmdAmountLocal*1;
            }
        })
        return amountPaidFromAcount;
    }
    function amountAllreadyPaidByAccount() {
        var amountPaidbyAcount = 0.00;
        if ($scope.txnMediaList.data == undefined || $scope.txnMediaList.data.length ===0) {
            return amountPaidbyAcount;
        }
        $scope.txnMediaList.data.forEach(function (txnMedia){
            if ((!txnMedia.txmdVoided) && (txnMedia.paymentMedia.paymName === 'Account')) {
                amountPaidbyAcount = amountPaidbyAcount*1 + txnMedia.paymentMedia.txmdAmountLocal*1;
            }
        })
        return amountPaidbyAcount;
    }
    $scope.accountBalance = function() {
        return amountPaidFromAccount()*1 - amountAllreadyPaidByAccount()*(-1);
    }
});
