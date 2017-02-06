/**
 * Created by arash on 12/09/2016.
 */
/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('debtorPaymentCtrl', function($scope, $state, $timeout, $stateParams, baseDataService,ngDialog, uiGridConstants, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI, CUSTOMER_GET_ACCOUNT_DEBT_URI, TXN_ADD_ACC_PAYMENT) {

    initPageData();
    initDebtList();
    initTxnMediaList();

    $scope.onMediaTypeChange = function () {
        var paymentMediaOfTypeUri = PAYMENT_MEDIA_OF_TYPE_URI + $scope.mediaType.id;
        baseDataService.getBaseData(paymentMediaOfTypeUri).then(function(response){
            $scope.paymentMediaSet = response.data;
            $scope.paymentMedia = baseDataService.populateSelectList($scope.paymentMedia,$scope.paymentMediaSet);
        });
    }


    function initPageData() {
        $scope.debtorPaymentForm = {};
        $scope.debtorPaymentForm.total = 0.00;
        $scope.debtorPaymentForm.amountDue = 0.00

        baseDataService.getBaseData(MEDIA_TYPE_ALL_URI).then(function(response){
            $scope.mediaTypeSet = response.data;
            $scope.mediaType = baseDataService.populateSelectList($scope.mediaType,$scope.mediaTypeSet);
            $scope.onMediaTypeChange();
        });
    }
    function initDebtList() {
        $scope.debtList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter: true,
            enableRowSelection:false,
            columnDefs: [
                {field: 'id', visible: false, enableCellEdit: false},
                {field: 'cadStartDate', displayName:'Start Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field: 'cadDueDate', displayName:'Due Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field: 'txhdTxnNr', displayName: 'Invoice No.', width: '15%'},
                {field: 'cadAmountDebt', displayName: 'Amount Owing', enableCellEdit: false, cellFilter: 'currency', width: '10%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field: 'paying', displayName: 'Paying', enableCellEdit: true, cellFilter: 'currency', width: '10%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field: 'balance', displayName: 'Balance', enableCellEdit: false, cellFilter: 'currency', width: '10%',footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum}
            ]
        }
        $scope.debtList.enableRowSelection = false;
        $scope.debtList.multiSelect = false;
        $scope.debtList.onRegisterApi = function (gridApi) {
            $scope.debtListGridApi = gridApi;
            gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
                if (colDef.name == 'paying') {
                    $scope.payingBeforeEditting = rowEntity.paying;
                }
            })
        };

        $scope.$on('uiGridEventEndCellEdit', function (event) {
            var debtDetail = event.targetScope.row.entity;
            if ( event.targetScope.col.field == 'paying') {
                if (debtDetail.balance < debtDetail.paying ) {
                    baseDataService.displayMessage('info','Warnning', 'paying amount is more than balance!!' );
                    debtDetail['paying'] = $scope.payingBeforeEditting;
                    return;
                }
                //debtDetail['balance'] = debtDetail.cadAmountDebt*1 - debtDetail.paying*1;
                totalTransaction();
            }
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
            /*
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
            */
        };

    }

    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                $scope.customer = value;
                //load the customer debt from database
                var getCustomerDebtUrl = CUSTOMER_GET_ACCOUNT_DEBT_URI + $scope.customer.id;
                baseDataService.getBaseData(getCustomerDebtUrl).then(function(response){
                    $scope.debtList.data = response.data;
                });

            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

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
            "txmdAmountLocal" : $scope.paymentAmount,
            "txmdVoided":false,
            "deleted" : false
        }
        $scope.txnMediaList.data.push(txnMedia);
        totalTransaction();
        //check if transaction is fully paid
        if ($scope.debtorPaymentForm.amountDue == 0) {
            baseDataService.displayMessage('yesNo','Sumbit!!','Do you want to Submit Transaction?').then(function(result){
                if (result) {
                    $scope.createTxnAccPayment();
                } else {
                    return;
                }
            });
        }
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

    function totalTransaction() {
        var debtList =  $scope.debtList.data;
        var totalPaying = 0.00;
        for (var i = 0; i < debtList.length; i++) {
            totalPaying = totalPaying*1 + debtList[i].paying*1 ;
        }
        $scope.debtorPaymentForm.total = totalPaying;
        $scope.debtorPaymentForm.amountDue = totalPaying - $scope.calculateAmountPaid();

        if (Math.abs($scope.debtorPaymentForm.amountDue) <= 0.05 ) {
            console.log('set rounding value');
            $scope.debtorPaymentForm.valueRounding = $scope.debtorPaymentForm.amountDue;
            $scope.debtorPaymentForm.amountDue = 0.00;
        }
        $scope.paymentAmount = $scope.maxPaymentAllowed()*1;
    }

    $scope.calculateAmountPaid = function() {
        if ($scope.txnMediaList == undefined || $scope.txnMediaList.data == undefined) {
            return 0.00;
        }
        var txnMediaList =  $scope.txnMediaList.data;
        var total = 0.00;
        for (var i = 0; i < txnMediaList.length; i++) {
            if (!txnMediaList[i].deleted) {
                total = total*1 + txnMediaList[i].txmdAmountLocal*1;
            }
        }
        return total;
    }

    $scope.createTxnAccPayment = function () {

        $scope.debtorPaymentForm.debtList = $scope.debtList.data;
        $scope.debtorPaymentForm.txnMediaList = $scope.txnMediaList.data;
        $scope.debtorPaymentForm.customer = $scope.customer;
        var rowObject = $scope.debtorPaymentForm;
        baseDataService.addRow(rowObject, TXN_ADD_ACC_PAYMENT).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listSaleTransaction');
            } else {
                alert('Not able to save Transaction. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.maxPaymentAllowed = function() {
        var maxPayment = (Math.round($scope.debtorPaymentForm.amountDue*Math.pow(10,1))/Math.pow(10,1)).toFixed(2);
        return maxPayment;
    }

});
