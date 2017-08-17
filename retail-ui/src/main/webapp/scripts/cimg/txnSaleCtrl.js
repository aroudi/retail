/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleCtrl', function($scope, $state, $timeout, $stateParams,viewMode, baseDataService, multiPageService,ngDialog, uiGridConstants, SUCCESS, FAILURE, TXN_ADD_URI, TXN_TYPE_QUOTE, TXN_TYPE_SALE,TXN_TYPE_INVOICE, TXN_STATE_FINAL, TXN_STATE_DRAFT, TXN_EXPORT_PDF, TXN_ADD_PAYMENT_URI, TXN_INVOICE_URI, TXN_MEDIA_SALE, TXN_MEDIA_DEPOSIT, INVOICE_EXPORT_PDF, PRODUCT_SALE_ITEM_GET_BY_SKU_URI, PRODUCT_SALE_ITEM_GET_BY_PROD_ID_URI, MEDIA_TYPE_GET_BYNAME_URI, PRICING_GRADE_DEFAULT, CUSTOMER_ALL_URI, PAYMENT_MEDIA_ALL_URI, TXN_EXPORT_PDF, PRODUCT_SALE_ITEM_SEARCH_URI) {

    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }

    $scope.isPageNew = baseDataService.getIsPageNew();
    /*
          *  by default it is a sale order until user select invoice option on the page.
          *  this variable indicates display mode which are InvoiceMode and SaleOrderMode
     */
    $scope.isInvoiceMode = false;
    $scope.exportSaleOrderToPdfUrl = TXN_EXPORT_PDF;
    $scope.allItemsSelected = false;
    $scope.exportInvoiceToPdfUrl = INVOICE_EXPORT_PDF;
    initPageData();
    initTxnDetail();
    initTxnMediaList();
    if ( $scope.txnHeaderForm.temporarySaved  && $scope.txnHeaderForm.txhdTxnType.categoryCode === 'TXN_TYPE_SALE') {
        checkIfItemsInvoiced();
    }

    function initPageData() {
        baseDataService.getBaseData(TXN_MEDIA_SALE).then(function(response){
            $scope.txnMediaTypeSale = response.data;
        });
        baseDataService.getBaseData(PRICING_GRADE_DEFAULT).then(function(response){
            $scope.customerGradeDefault = response.data;
        });
        baseDataService.getBaseData(TXN_MEDIA_DEPOSIT).then(function(response){
            $scope.txnMediaTypeDeposit = response.data;
        });
        $scope.model={};
        if ( baseDataService.getIsPageNew()) {
            //get txn_type from state params.
            $scope.txnType = $stateParams.txnType;
            $scope.txnHeaderForm = {};
            $scope.model.customer={};
            $scope.txnHeaderForm.txhdValueCredit = 0.00;
            $scope.txnHeaderForm.id = -1;
            $scope.txnHeaderForm.convertedToInvoice = false;
            $scope.txnHeaderForm.convertedToTxnSale = false;
            $scope.txnHeaderForm.temporarySaved = false;
        } else {
            $scope.txnHeaderForm = angular.copy(baseDataService.getRow());
            $scope.model.customer = $scope.txnHeaderForm.customer;
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
            $scope.paymentAmount = maxPaymentAllowed()*1;
            if ($scope.txnHeaderForm.temporarySaved) {
                $scope.isPageNew = true;
                if ($scope.txnHeaderForm.txhdTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
                    $scope.isInvoiceMode = true;
                }
            }
        }
        $scope.sendEmail = false;
        if ($scope.isPageNew && !$scope.txnHeaderForm.temporarySaved) {
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
            baseDataService.getBaseData(TXN_TYPE_INVOICE).then(function(response){
                if ($scope.txnType == 'txnInvoice') {
                    $scope.txnHeaderForm.txhdTxnType = response.data;
                    $scope.isInvoiceMode = true;
                }
            });
            //DEFUALT STATE IS DRAFT.
            baseDataService.getBaseData(TXN_STATE_DRAFT).then(function(response){
                $scope.txnHeaderForm.txhdState = response.data;
            });
        }
        baseDataService.getBaseData(PAYMENT_MEDIA_ALL_URI).then(function(response){
            //$scope.mediaTypeSet = response.data;
            //$scope.mediaType = baseDataService.populateSelectList($scope.mediaType,$scope.mediaTypeSet);
            //$scope.onMediaTypeChange();
            $scope.paymentMediaSet = response.data;
            $scope.paymentMedia = baseDataService.populateSelectList($scope.paymentMedia,$scope.paymentMediaSet);

        });
        baseDataService.getBaseData(MEDIA_TYPE_GET_BYNAME_URI+'Account').then(function(response){
            $scope.mediaTypeAccount = response.data;
        });
        baseDataService.getBaseData(MEDIA_TYPE_GET_BYNAME_URI+'Cash').then(function(response){
            $scope.mediaTypeCash = response.data;
        });
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.customerSet = response.data;
            if ($scope.customerSet.length > 0) {
                var customer = {
                    "id" : -1,
                    "companyName" : "Not Selected"
                }
                $scope.customerSet.unshift(customer);
            }
            //$scope.customer = baseDataService.populateSelectList($scope.customer,$scope.customerSet);
            //$scope.onCustomerChange();
        });
    }

    /*
    $scope.onMediaTypeChange = function () {
        var paymentMediaOfTypeUri = PAYMENT_MEDIA_OF_TYPE_URI + $scope.mediaType.id;
        baseDataService.getBaseData(paymentMediaOfTypeUri).then(function(response){
            $scope.paymentMediaSet = response.data;
            $scope.paymentMedia = baseDataService.populateSelectList($scope.paymentMedia,$scope.paymentMediaSet);
        });
    }
    */

    function initTxnDetail() {
        var rowtpl='<div ng-class="{\'blue\':row.entity.txdeItemVoid==false, \'red\':row.entity.txdeItemVoid==true }"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
        $scope.txnDetailList = {
            enableFiltering: false,
            showGridFooter: true,
            showColumnFooter: true,
            enableRowSelection:true,
            gridFooterTemplate:"<div id=\"currency-default\"> Total:{{grid.appScope.txnHeaderForm.txhdValueNett | currency}}</div>",
            rowTemplate : rowtpl,
            columnDefs: [
                {field: 'id', visible: false, enableCellEdit: false},
                {field: 'product.prodSku', displayName: 'SKU', enableCellEdit: false, enableFiltering:false,width: '10%',
                    cellTooltip: function(row,col) {
                        return row.entity.product.prodSku + '\n' +
                            'Qty In Stock:' + row.entity.product.stockQty + '\n';
                    }
                },
                {field: 'txdeProdName', displayName: 'Name', enableCellEdit: true, enableFiltering:false,width: '20%',
                    cellTooltip: function(row,col) {
                        return row.entity.txdeProdName + '\n' +
                            'Qty In Stock:' + row.entity.product.stockQty + '\n';
                    }
                },
                {field: 'unitOfMeasure.unomDesc', displayName: 'Size', enableCellEdit: false,enableFiltering:false, width: '5%',
                    cellTooltip: function(row,col) {
                        return 'Qty In Stock:' + row.entity.product.stockQty;
                    }
                },
                //{field: 'txdeValueLine', displayName: 'Cost', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                //{field: 'txdeValueProfit', displayName: 'Profit', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'txdeValueGross', displayName: 'Price', cellFilter: 'currency',enableFiltering:false, width: '7%'},
                {field: 'txdeQuantitySold', displayName: 'Qty', type: 'number', enableFiltering:false,width: '6%',
                    cellTooltip: function(row,col) {
                        return 'Qty In Stock:' + row.entity.product.stockQty + '\n' +
                               'Total Qty Sold:' +  row.entity.txdeQuantitySold + '\n' +
                               'Total Qty Invoiced:' +  row.entity.txdeQtyTotalInvoiced + '\n' +
                               'Total back order qty:' +  row.entity.txdeQtyBackOrder + '\n' +
                               'Qty on Order:' +  row.entity.txdeQtyOrdered + '\n' +
                               'Qty Received:' +  row.entity.txdeQtyReceived + '\n' +
                               'Qty Refund:' +  row.entity.txdeQtyTotalRefund
                    }
                },
                {field: 'txdeQtyInvoiced', displayName: 'Invoice', type: 'number',enableFiltering:false, width: '6%'},
                {field: 'originalQuantity', visible: false, type: 'number',enableFiltering:false},
                {field: 'txdeQtyBalance', enableCellEdit: false, displayName: 'Balance',enableFiltering:false, type: 'number', width: '6%',
                    cellTooltip: function(row,col) {
                        return 'Qty In Stock:' + row.entity.product.stockQty + '\n' +
                            'Total Qty Sold:' +  row.entity.txdeQuantitySold + '\n' +
                            'Total Qty Invoiced:' +  row.entity.txdeQtyTotalInvoiced + '\n' +
                            'Total back order qty:' +  row.entity.txdeQtyBackOrder + '\n' +
                            'Qty on Order:' +  row.entity.txdeQtyOrdered + '\n' +
                            'Qty Received:' +  row.entity.txdeQtyReceived + '\n' +
                            'Qty Refund:' +  row.entity.txdeQtyTotalRefund
                    }
                },
                {field: 'txdeQtyBackOrder',enableCellEdit: true, displayName: 'Back Order', enableFiltering:false,type: 'number', width: '7%',
                    cellTooltip: function(row,col) {
                        return 'Qty In Stock:' + row.entity.product.stockQty + '\n' +
                            'Total Qty Sold:' +  row.entity.txdeQuantitySold + '\n' +
                            'Total Qty Invoiced:' +  row.entity.txdeQtyTotalInvoiced + '\n' +
                            'Total back order qty:' +  row.entity.txdeQtyBackOrder + '\n' +
                            'Qty on Order:' +  row.entity.txdeQtyOrdered + '\n' +
                            'Qty Received:' +  row.entity.txdeQtyReceived + '\n' +
                            'Qty Refund:' +  row.entity.txdeQtyTotalRefund
                    }
                },
                {field: 'calculatedLineValue', enableFiltering:false,displayName: 'Amount', enableCellEdit: false, cellFilter: 'currency', width: '8%'},
                {field: 'calculatedLineTax', enableFiltering:false,displayName: 'Tax', cellFilter: 'currency', footerCellFilter: 'currency',enableCellEdit: false, width: '7%'},
                {field: 'txdePriceSold', enableFiltering:false, displayName: 'Total', cellFilter: 'currency', footerCellFilter: 'currency', enableCellEdit: false, width: '10%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Item" ng-show="grid.appScope.isTxnLineVoidable(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidItem(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" ng-show="row.entity.id < 0" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a>', width: '8%'}

            ]
        }
        $scope.txnDetailList.enableRowSelection = true;
        $scope.txnDetailList.multiSelect = true;
        $scope.txnDetailList.enableSelectAll = false;
        //$scope.txnDetailList.noUnselect = true;
        //
        $scope.txnDetailList.onRegisterApi = function (gridApi) {
            gridApi.core.registerColumnsProcessor(hideInvoiceColumns);
            $scope.txnDetailGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                //$scope.selectedTxnDetailRow = row.entity;
                //invoice option must be available only for Sale Order (type=TXN_SALE)
                if ($scope.txnHeaderForm.txhdTxnType.categoryCode != 'TXN_TYPE_SALE') {
                    gridApi.selection.unSelectRow(row.entity);
                    return;
                }
                //check if it had been invoiced totally before, prevent user from selecting the row
                if (row.entity.txdeQtyTotalInvoiced*1  >= row.entity.txdeQuantitySold*1) {
                    gridApi.selection.unSelectRow(row.entity);
                    return;
                }
                if (row.isSelected) {
                    row.entity.invoiced = true;
                } else {
                    row.entity.invoiced = false;
                    row.entity.txdeQtyInvoiced = 0;
                }
                checkIfItemSelected();
                evaluatRowItem(row.entity, false);
                totalTransaction();
            });
            gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
                if (colDef.name === 'txdeQuantitySold') {
                    $scope.txdeQuantitySoldBeforeEditting = rowEntity.txdeQuantitySold;
                }
                if (colDef.name === 'txdeQtyInvoiced') {
                    $scope.txdeQtyInvoicedBeforeEditting = rowEntity.txdeQtyInvoiced;
                }
                if (colDef.name === 'txdeValueGross') {
                    $scope.txdeValueGrossBeforeEditting = rowEntity.txdeValueGross;
                }
                if (colDef.name === 'txdeQtyBackOrder') {
                    $scope.txdeQtyBackOrderBeforeEditting = rowEntity.txdeQtyBackOrder;
                }
            })
            //hide Invoice and Balance column for Quote and Invoice.
            function hideInvoiceColumns(columns){
                if ( $scope.txnHeaderForm == undefined || $scope.txnHeaderForm.txhdTxnType == undefined) {
                    return;
                }
                if ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_SALE') {
                    columns.forEach(function(column){
                        if (column.field === 'txdeQtyInvoiced' || column.field ==='txdeQtyBalance' || column.field === 'txdeQtyBackOrder') {
                            column.visible = true;
                        }

                        if (column.field === 'product.prodSku') {
                            column.width = '10%';
                        }
                        if (column.field === 'product.prodName') {
                            column.width = '20%';
                        }
                    });
                    return columns;
                }
                columns.forEach(function(column){
                    if (column.field === 'txdeQtyInvoiced' || column.field ==='txdeQtyBalance' || column.field === 'txdeQtyBackOrder') {
                        column.visible = false;
                    }

                    if (column.field === 'product.prodSku') {
                        column.width = '17%';
                    }
                    if (column.field === 'product.prodName') {
                        column.width = '27%';
                    }
                });
                return columns;
            }
        };

        $scope.$on('uiGridEventEndCellEdit', function (event) {
            var txnDetail = event.targetScope.row.entity;
            if ( event.targetScope.col.field == 'txdeValueGross') {
                //check if we already had invoiced this item. if so user can not change the price.
                if (txnDetail.txdeQtyTotalInvoiced >0 ) {
                    baseDataService.displayMessage('info','Warnning', 'The Item is invoiced. you can not change the price.' );
                    txnDetail['txdeValueGross'] = $scope.txdeValueGrossBeforeEditting;
                    return;
                }
                var originalPrice = calculateSalePrice(txnDetail.product);
                if (txnDetail.txdeValueGross < originalPrice ) {
                    baseDataService.displayMessage('info','Warnning', 'You can not set the price below original which is $' + originalPrice );
                    txnDetail['txdeValueGross'] = $scope.txdeValueGrossBeforeEditting;
                    return;
                }
                txnDetail['txdeQtyBalance'] = calculateBalance(txnDetail);//txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1);
            }

            if ( event.targetScope.col.field === 'txdeQuantitySold') {
                txnDetail['txdeQtyBackOrder'] = calculateItemBackOrder(txnDetail);
            }

            if ( event.targetScope.col.field === 'txdeQtyBackOrder') {
                if (txnDetail.txdeQtyBackOrder > txnDetail.txdeQuantitySold) {
                    baseDataService.displayMessage('info','Warnning', 'back order qty is bigger than sale quantity');
                    txnDetail['txdeQtyBackOrder'] = $scope.txdeQtyBackOrderBeforeEditting;
                    return;

                }
            }

            // check invoiced qty. if bigger than qty available display a warnning.
            if ( event.targetScope.col.field === 'txdeQtyInvoiced') {

                //if we have received items then check against it
                if (txnDetail.status!=undefined && (txnDetail.status.categoryCode === 'SO_STATUS_RECEIVED' || txnDetail.status.categoryCode === 'SO_STATUS_PARTIAL_REC')) {
                    //if good received and user try to invoice more than qty received, display a warnning
                    if (txnDetail.txdeQtyReceived > 0 && txnDetail.txdeQtyInvoiced > txnDetail.txdeQtyReceived) {
                        baseDataService.displayMessage('yesNo','Warnning','Qty entered is more than available qty('+ txnDetail.txdeQtyReceived +') Do you want to continue?').then(function(result){
                            if (result) {
                                //continue.....
                            } else {
                                txnDetail['txdeQtyInvoiced'] = $scope.txdeQtyInvoicedBeforeEditting;
                                return;
                            }
                        });
                    }
                } else {
                    //we need to check against availabl qty in stock
                    if (txnDetail.txdeQtyInvoiced > txnDetail.product.stockQty) {
                        baseDataService.displayMessage('yesNo','Warnning','Qty entered is more than available qty('+ txnDetail.product.stockQty +'). Do you want to continue?').then(function(result){
                            if (result) {
                                //continue.....
                            } else {
                                txnDetail['txdeQtyInvoiced'] = $scope.txdeQtyInvoicedBeforeEditting;
                                return;
                            }
                        });
                    }
                }
            }

            //cellData = txnDetail[event.targetScope.col.field];
            //if it has not been invoiced before and total invoiced is undefined:
            if (txnDetail['txdeQtyTotalInvoiced'] == undefined) {
                txnDetail['txdeQtyTotalInvoiced'] = 0;
            }
            var newBalance = calculateBalance(txnDetail);//txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1);
            if (newBalance < 0) {
                baseDataService.displayMessage('info','Invalid Qty', 'Invoiced Quantity is higher than total Quantity !!!');
                if (event.targetScope.col.field == 'txdeQuantitySold') {
                    //txnDetail.txdeQuantitySold = $scope.txdeQuantitySoldBeforeEditting;
                    //txnDetail[event.targetScope.col.field] = $scope.txdeQuantitySoldBeforeEditting;
                    txnDetail[event.targetScope.col.field] = txnDetail['originalQuantity'];
                }
                if (event.targetScope.col.field == 'txdeQtyInvoiced') {
                    //txnDetail.txdeQtyInvoice = $scope.txdeQtyInvoicedBeforeEditting;
                    txnDetail['txdeQtyInvoiced'] = 0;
                }
                //return;
            }
            newBalance = calculateBalance(txnDetail);
            if (txnDetail['txdeQtyInvoiced'] > 0) {
                txnDetail['invoiced'] = true;
            } else {
                txnDetail['invoiced'] = false;
            }
            checkIfItemsInvoiced();
            //check the mode.
            var quantity = 0;
            if ($scope.isInvoiceMode) {
                quantity = txnDetail.txdeQtyInvoiced;
            } else {
                quantity = txnDetail.txdeQuantitySold;
            }
            if ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_INVOICE') {
                quantity = txnDetail.txdeQuantitySold;
                txnDetail.txdeQtyInvoiced =  quantity;
                txnDetail.txdeQtyBalance =  0;
            }
            txnDetail['txdeQtyBalance'] = newBalance;
            txnDetail['txdeValueNet'] =  (txnDetail.txdeValueGross * txnDetail.txdeTax)*1 + txnDetail.txdeValueGross*1;
            txnDetail['txdePriceSold'] = quantity * txnDetail.txdeValueNet;
            txnDetail['calculatedLineValue'] = txnDetail.txdeValueGross * quantity;
            txnDetail['calculatedLineTax'] = txnDetail.calculatedLineValue * txnDetail.txdeTax;
            totalTransaction();
        })
        if ((!$scope.isPageNew) || $scope.txnHeaderForm.temporarySaved ) {
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
                //{field:'txmdType.displayName',displayName:'Type', visible:true, enableCellEdit:false, width: '10%'},
                {field:'paymentMedia.paymName',displayName:'Payment Media', visible:true, enableCellEdit:false, width: '45%',
                    cellTooltip: function(row,col) {
                        return row.entity.paymentMedia.paymName
                    }
                },
                {field:'txmdAmountLocal', displayName:'Amount', visible:true, cellFilter:'currency', footerCellFilter:'currency', /*aggregationType: uiGridConstants.aggregationTypes.sum, */ enableCellEdit:false, width: '35%'},
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Tender" ng-show="grid.appScope.isTxnLineVoidable(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidTender(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" ng-show="row.entity.id < 0" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeTxnMedia(row)"></i></a>', width: '15%'}

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

        if ((!$scope.isPageNew) || $scope.txnHeaderForm.temporarySaved ) {
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
                $scope.model.customer = value;
                //var mediaType;
                $scope.txnHeaderForm.txhdDlvAddress = $scope.model.customer.address2;
                /*
                if ($scope.model.customer.customerType.categoryCode == 'CUSTOMER_TYPE_ACCOUNT') {
                    mediaType = $scope.mediaTypeAccount;
                } else {
                    mediaType = $scope.mediaTypeCash;
                }
                */
                //$scope.mediaType = baseDataService.populateSelectList(mediaType,$scope.mediaTypeSet);
                //$scope.onMediaTypeChange();
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.onCustomerChange = function () {
        //var mediaType;
        $scope.txnHeaderForm.txhdDlvAddress = $scope.model.customer.address2;
        $scope.txnHeaderForm.txhdEmailTo = $scope.model.customer.email;
        /*
        if ($scope.model.customer.customerType.categoryCode == 'CUSTOMER_TYPE_ACCOUNT') {
            mediaType = $scope.mediaTypeAccount;
        } else {
            mediaType = $scope.mediaTypeCash;
        }
        $scope.mediaType = baseDataService.populateSelectList(mediaType,$scope.mediaTypeSet);
        $scope.onMediaTypeChange();
        */
    }

    $scope.searchProduct = function () {
        var searchStr = $scope.searchBySku;
        if (searchStr === undefined || searchStr === null || searchStr === "") {
            searchStr = "@ALL@";
        }
        $scope.searchBySku = "";
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productSaleItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return PRODUCT_SALE_ITEM_SEARCH_URI + searchStr}}
        }).then (function (result){
                //alert('returned value = ' + value);
                if (result === 'NO_RESULT' || result === undefined) {
                    baseDataService.displayMessage('info','Not Found!','Product not found!!!');
                    return;
                }
                if (result != undefined) {
                    for (var i = 0; i < result.length; i++) {
                        var selectedProduct = result[i];
                        //check if product is already selected.
                        /*
                        if (checkIfProductHasBeenSelected(selectedProduct)) {
                            continue;
                        }
                        */
                        //get product detail from server
                        var searchUri = PRODUCT_SALE_ITEM_GET_BY_PROD_ID_URI + selectedProduct.id;
                        baseDataService.getBaseData(searchUri).then(function(response){
                            var product = response.data;
                            if (response.data === null || response.data === undefined ) {
                                baseDataService.displayMessage('info','Warning!','product not found!!!');
                                //continue;
                            }
                            var txnDetail = createTxnDetail();
                            txnDetail.product = product;
                            txnDetail.unitOfMeasure = txnDetail.product.sellPrice.unitOfMeasure;
                            txnDetail.txdeQtyTotalInvoiced =  0;
                            txnDetail.txdeProdName = txnDetail.product.prodName;
                            txnDetail.txdeQuantitySold =  1;
                            txnDetail.originalQuantity =  0;
                            if ($scope.txnHeaderForm.txhdTxnType.categoryCode === 'TXN_TYPE_SALE') {
                                txnDetail.txdeQtyBackOrder = calculateItemBackOrder(txnDetail);
                            }
                            if ($scope.txnHeaderForm.txhdTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
                                txnDetail.txdeQtyInvoiced =  1;
                                txnDetail.txdeQtyBalance =  0;
                                txnDetail.invoiced = true;
                            } else {
                                txnDetail.txdeQtyInvoiced =  0;
                                txnDetail.txdeQtyBalance =  txnDetail.txdeQuantitySold;
                            }
                            evaluatRowItem(txnDetail, true);
                            $scope.txnDetailList.data.push(txnDetail);
                            totalTransaction();
                        });
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.searchProductBySku = function(keyEvent) {
        if (keyEvent.which != 13) {
            return
        }
        $scope.searchProduct();
    }
    
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
    function evaluatRowItem (txnDetail, newAdded) {
        //if cost is not defined for product, set it to sell price.
        if (txnDetail.product.costPrice == undefined || txnDetail.product.costPrice == null) {
            txnDetail.product.costPrice = txnDetail.product.sellPrice;
        }
        txnDetail.txdeTax = calculateTaxRate(txnDetail.product);
        txnDetail.txdeValueLine = calculateBasePrice(txnDetail.product);
        if (newAdded == true) {
            txnDetail.txdeValueGross =  calculateSalePrice(txnDetail.product);//txnDetail.txdeValueProfit*1 + txnDetail.txdeValueLine*1;
        }
        txnDetail.txdeProfitMargin = ((txnDetail.txdeValueGross - txnDetail.txdeValueLine)*1)/100; //getProfitMargin();
        txnDetail.txdeValueProfit = txnDetail.txdeValueGross*1 - txnDetail.txdeValueLine*1; //txnDetail.txdeValueLine*txnDetail.txdeProfitMargin;

        txnDetail.txdeValueNet =  (txnDetail.txdeValueGross * txnDetail.txdeTax)*1 + txnDetail.txdeValueGross*1;
        txnDetail.txdeQtyBalance = calculateBalance(txnDetail);//txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 + txnDetail.txdeQtyInvoiced*1);

        var quantity = 0;
        if ($scope.isInvoiceMode) {
            quantity = txnDetail.txdeQtyInvoiced;
        } else {
            quantity = txnDetail.txdeQuantitySold;
        }
        if ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_INVOICE') {
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

    function calculateSalePrice (productSaleItem) {
        var priceRule='';
        var rate;
        //get default price rule if customer is null
        if ($scope.model.customer == undefined || $scope.model.customer==null || $scope.model.customer.grade == undefined) {
            if ($scope.customerGradeDefault != undefined && $scope.customerGradeDefault != null && $scope.customerGradeDefault.rate != undefined) {
                priceRule = $scope.customerGradeDefault.description;
                rate = $scope.customerGradeDefault.rate;
            }
        } else {
            priceRule = $scope.model.customer.grade.description;
            rate = $scope.model.customer.grade.rate;
        }
        switch (priceRule) {
            case 'Cost + %' :
                return productSaleItem.costPrice.prcePrice*(1 + rate*1);
            case 'Cost + $' :
                return productSaleItem.costPrice.prcePrice*1 + rate*1;
            case 'Fixed $' :
                return rate;
            case 'RRP - $' :
                return productSaleItem.sellPrice.prcePrice*1 - rate*1;
            case 'RRP - %' :
                return productSaleItem.sellPrice.prcePrice*(1 - rate*1);
            default:
                return productSaleItem.sellPrice.prcePrice;
        }
    }

    function calculateBasePrice (productSaleItem) {
        var priceRule='';
        var rate;
        //get default price rule if customer is null
        if ($scope.model.customer == undefined || $scope.model.customer==null || $scope.model.customer.grade == undefined) {
            if ($scope.customerGradeDefault != undefined && $scope.customerGradeDefault != null && $scope.customerGradeDefault.rate != undefined) {
                priceRule = $scope.customerGradeDefault.description;
                rate = $scope.customerGradeDefault.rate;
            }
        } else {
            priceRule = $scope.model.customer.grade.description;
            rate = $scope.model.customer.grade.rate;
        }
        switch (priceRule) {
            case 'Cost + %' :
                return productSaleItem.costPrice.prcePrice*1;
            case 'Cost + $' :
                return productSaleItem.costPrice.prcePrice*1;
            case 'Fixed $' :
                return rate;
            case 'RRP - $' :
                return productSaleItem.sellPrice.prcePrice*1;
            case 'RRP - %' :
                return productSaleItem.sellPrice.prcePrice*1;
            default:
                return productSaleItem.sellPrice.prcePrice;
        }
    }

    $scope.addTxnMedia= function() {
        //check if selected Media Type is Account. then we need to check if customer is account customer and has enough credit
        if ($scope.paymentMedia.paymName === 'Account') {
            //check if we are not in invoice mode then do nothing
            if (!$scope.isInvoiceMode) {
                baseDataService.displayMessage('info','Warning!!','Account Payment is only available for Invoice');
                return;
            }
            if ($scope.model.customer.customerType.categoryCode != 'CUSTOMER_TYPE_ACCOUNT') {
                baseDataService.displayMessage('info','Warning!!','Account Payment is only applicable for Account customers');
                return;
            }
            //check if customer is not on hold
            if ($scope.model.customer.customerStatus.categoryCode === 'CUSTOMER_STATUS_ON_HOLD') {
                baseDataService.displayMessage('info','Warning!!','customer is on hold. you can not pay from credit');
                return;
            }
            //check if we have enough credit
            var newCredit = $scope.txnHeaderForm.txhdValueCredit*1  + $scope.paymentAmount*1;
            if (newCredit > $scope.model.customer.remainCredit) {
                baseDataService.displayMessage('info','Warning!!','amount exceeds remain credit.');
            }
        }
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
        if ($scope.paymentMedia.paymName === 'Account') {
            $scope.txnHeaderForm.txhdValueCredit = $scope.txnHeaderForm.txhdValueCredit*1 + $scope.paymentAmount*1
        }
        totalTransaction();
        //check if transaction is fully paid
        if ($scope.txnHeaderForm.txhdValueDue == 0) {
            baseDataService.displayMessage('yesNo','Sumbit!!','Do you want to Submit Transaction?').then(function(result){
                if (result) {
                    if ($scope.showSubmitButtom()) {
                        $scope.createTransactionSale();
                    }
                    if ($scope.showInvoiceButtom()) {
                        $scope.invoiceTransactionSale();
                    }
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
                if ((!$scope.isInvoiceMode) && ($scope.paymentMedia.paymName === 'Account')) {
                    $scope.txnHeaderForm.txhdValueCredit = $scope.txnHeaderForm.txhdValueCredit*1 - $scope.paymentAmount*1
                }

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
        $scope.txnHeaderForm.customer = $scope.model.customer;

        //check if we need to send email.
        if (!checkEmailIsValid()) {
            return;
        }
        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                if ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_QUOTE' || $scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_SALE') {
                    if ($scope.isPageNew || !$scope.isPageNew && $scope.txnHeaderForm.convertedToTxnSale) {
                        $scope.txnHeaderForm.id=addResponse.info;
                        $scope.exportToPdf(TXN_EXPORT_PDF);
                    }
                    $state.go('dashboard.listSaleTransaction');
                }

                if ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_INVOICE') {
                    if ($scope.isPageNew ) {
                        $scope.txnHeaderForm.id=addResponse.info;
                        $scope.exportToPdf($scope.exportInvoiceToPdfUrl);
                    }
                    if (!$scope.isPageNew && $scope.txnHeaderForm.convertedToInvoice) {
                        baseDataService.displayMessage("info","Invoice Number", "Invoice saved with number: " + addResponse.info);
                        $state.go('dashboard.listInvoice');
                    }
                    $state.go('dashboard.listInvoice');
                }
                //$state.go('dashboard.listSaleTransaction');
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
                valueTax = valueTax*1 + txnDetailList[i].txdeTax*txnDetailList[i].txdeValueGross*quantity*1;
            }
        }
        $scope.txnHeaderForm.txhdValueNett = valueNett;
        $scope.txnHeaderForm.txhdValueGross = valueGross;
        $scope.txnHeaderForm.txhdValueTax = valueTax;
        $scope.txnHeaderForm.txhdValueDue = valueNett - $scope.calculateAmountPaid();
        //check if amount due is less than 5 cents, then add it to rounding
        if (Math.abs($scope.txnHeaderForm.txhdValueDue) < 0.06 ) {
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
        //if item has been invoiced before then we can not void item.
        if (row.entity.txdeQtyTotalInvoiced >0) {
            baseDataService.displayMessage("info","Warning!!","Item has been invoiced. you can not void it!!!");
            return;
        }
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to void this item??').then(function(result){
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
    };
    $scope.createTransactionSaleDraft = function() {
        baseDataService.getBaseData(TXN_STATE_DRAFT).then(function(response){
            $scope.txnHeaderForm.txhdState = response.data;
            $scope.createTransactionSale();
        });
    };

    $scope.convertToSaleTxn = function() {
        //when converting to txn_sale, we need to change the state to draft.
        if ($scope.txnHeaderForm.convertedToTxnSale) {
            baseDataService.getBaseData(TXN_TYPE_SALE).then(function(response){
                $scope.txnHeaderForm.txhdTxnType = response.data;
                refreshBackOrderColumn();
            });
            baseDataService.getBaseData(TXN_STATE_DRAFT).then(function(response){
                $scope.txnHeaderForm.txhdState = response.data;
            });
        } else {
            baseDataService.getBaseData(TXN_TYPE_QUOTE).then(function(response){
                $scope.txnHeaderForm.txhdTxnType = response.data;
            });
        }
        $scope.txnDetailGridApi.core.refresh();
    };

    $scope.convertToInvoice = function() {
        //when converting to invoice, we need to change the state to draft.
        if ($scope.txnHeaderForm.convertedToInvoice) {
            baseDataService.getBaseData(TXN_TYPE_INVOICE).then(function(response){
                $scope.txnHeaderForm.txhdTxnType = response.data;
                $scope.isInvoiceMode = true;
            });
            if ($scope.txnDetailList.data != undefined) {
                for (var i=0; i<$scope.txnDetailList.data.length; i++) {
                    var txnDetail = $scope.txnDetailList.data[i];
                    var quantity = txnDetail.txdeQuantitySold;
                    txnDetail.txdeQtyInvoiced =  quantity;
                    txnDetail.txdeQtyBalance =  0;
                    txnDetail.invoiced = true;
                }
            }
        } else {
            baseDataService.getBaseData(TXN_TYPE_QUOTE).then(function(response){
                $scope.txnHeaderForm.txhdTxnType = response.data;
            });
        }
        $scope.txnDetailGridApi.core.refresh();
    };

    $scope.isTxnLineVoidable = function (row) {
        /* we dont have void at this point. we can only deleted a new added tender but not void tender.
        if ( ($scope.txnHeaderForm.txhdState.categoryCode != 'TXN_STATE_FINAL') && (row.entity.id > 0)) {
            return true;
        }
        */
        return false;
    };
    $scope.isTxnSaleAndFinal = function () {
        if ($scope.txnHeaderForm == undefined || $scope.txnHeaderForm.status == undefined) {
            return false;
        }
        if ($scope.txnHeaderForm.status.categoryCode == 'SO_STATUS_FINAL' && $scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_SALE') {
            return true;
        }
    };
    $scope.isTxnSaleAndPending = function () {
        if ((!$scope.isPageNew) && $scope.txnHeaderForm.status.categoryCode != 'SO_STATUS_FINAL' && $scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_SALE') {
            return true;
        }
        return false;
    };
    $scope.exportToPdf = function(url) {

        var exportUrl = url + $scope.txnHeaderForm.id;
        baseDataService.pdfViewer(exportUrl);
        //$state.go('dashboard.pdfViewer');
    };

    $scope.addPayment = function () {

        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;
        $scope.txnHeaderForm.customer = $scope.model.customer;
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
    };

    /**
     *
     * @param mode true:invoice false:saleOrder
     */
    function changeToInvoiceMode() {
        var mode = $scope.isInvoiceMode;

        //recalculate txnDetail rows.
        $scope.txnDetailList.data.forEach(function (row){
            evaluatRowItem(row, false);
        })

        if (mode) {
            //copy existing medias
            var invoiceMediaList= [];
            $scope.txnMediaListBackup = angular.copy($scope.txnMediaList.data);
            $scope.txnMediaList.data.forEach(function (row){
                //we need to display new added media and also deposit as well.
                if ((!row.deleted)&&(row.id < 0 || row.txmdType.categoryCode == 'TXN_MEDIA_DEPOSIT')) {
                    invoiceMediaList.push(row);
                }
            })
            $scope.txnMediaList.data = invoiceMediaList;
        } else {
            var saleOrderMediaList = [];
            //make list from backup
            $scope.txnMediaListBackup.forEach(function (row){
                //if not row deleted
                if ( !(row.deleted)) {
                    saleOrderMediaList.push(row);
                }
            })
            //copy new added rows to the backup one.
            $scope.txnMediaList.data.forEach(function (row){
                //for new added rows
                if ( !(row.id > 0 || row.deleted)) {
                    //if row not exists in backup then add it
                    if (baseDataService.getArrIndexOf($scope.txnMediaListBackup, row)<0) {
                        saleOrderMediaList.push(row);
                    }
                }
            })
            //now set the data to backup
            $scope.txnMediaList.data = saleOrderMediaList;
        }
        totalTransaction();
    };
    $scope.invoiceTransactionSale = function () {
        if (!checkEmailIsValid()) {
            return;
        }
        //check if we have outstanding amount to pay
        if (maxPaymentAllowed() > 0) {
            baseDataService.displayMessage("info","Warning", "Payment is due!!!");
            return;
        }
        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;

        $scope.txnHeaderForm.customer = $scope.model.customer;
        var rowObject = $scope.txnHeaderForm;
        baseDataService.addRow(rowObject, TXN_INVOICE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                //baseDataService.displayMessage("info","Invoice Number", "Invoice saved with number: " + addResponse.info);
                $scope.txnHeaderForm.id=addResponse.info;
                $scope.exportToPdf($scope.exportInvoiceToPdfUrl);
                $state.go('dashboard.listInvoice');
            } else {
                alert('Not able to save Transaction. ' + addResponse.message);
            }
        });
        return;
    };

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
    };

    function checkIfItemsInvoiced(){
        var invoiceModeBeforeSelection = $scope.isInvoiceMode;
        var itemsInvoiced = false;
        for (var i=0; i<$scope.txnDetailList.data.length; i++) {
            var txnDetail = $scope.txnDetailList.data[i];
            if (txnDetail.txdeQtyInvoiced > 0) {
                itemsInvoiced = true;
                break;
            }
        }
        if (itemsInvoiced) {
            $scope.isInvoiceMode = true;
        } else {
            $scope.isInvoiceMode = false;
        }
        if ($scope.txnHeaderForm.temporarySaved) {
            return;
        }
        if (invoiceModeBeforeSelection != $scope.isInvoiceMode) {
            changeToInvoiceMode();
        }
    }

    $scope.showSubmitButtom = function() {
        return (!$scope.isTxnSaleAndFinal()) && !($scope.isInvoiceMode || isInvoiceViewMode() );
    };

    $scope.showAddPaymentButtom = function() {
        return ($scope.showSubmitButtom() || $scope.showInvoiceButtom()) && !$scope.isViewMode && ($scope.txnHeaderForm.txhdValueDue > 0)
    };
    $scope.showInvoiceButtom = function() {
        return ($scope.isInvoiceMode) && (!isInvoiceViewMode());
    };

    function checkIfProductHasBeenSelected(product) {

        for (var i = 0; i<$scope.txnDetailList.data.length; i++) {
            if ( (!($scope.txnDetailList.data[i].deleted || $scope.txnDetailList.data[i].txdeItemVoid))&&($scope.txnDetailList.data[i].product.id == product.id)) {
                return true;
            }
        }
        return false;
    }

    function maxPaymentAllowed() {
        var maxPayment = (Math.round($scope.txnHeaderForm.txhdValueDue*Math.pow(10,1))/Math.pow(10,1)).toFixed(2);
        return maxPayment;
    }

    $scope.maxPaymentAllowed = function() {
        return maxPaymentAllowed();
    };

    function calculateBalance(txnDetail) {
        if (txnDetail.txdeQtyTotalRefund == undefined) {
            txnDetail.txdeQtyTotalRefund = 0.00;
        }
        if ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_INVOICE') {
            return 0;
        }
        return txnDetail.txdeQuantitySold*1 - (txnDetail.txdeQtyTotalInvoiced*1 - txnDetail.txdeQtyTotalRefund*1 + txnDetail.txdeQtyInvoiced*1);
    };

    //check if we are in invoice viewing mode.
    function isInvoiceViewMode() {
        if ($scope.txnHeaderForm == undefined||  $scope.txnHeaderForm.txhdTxnType == undefined) {
            return false;

        }
        return ($scope.txnHeaderForm.txhdTxnType.categoryCode == 'TXN_TYPE_INVOICE') && (!$scope.isPageNew) && (!$scope.txnHeaderForm.convertedToInvoice)
    };
    function selectAllRowsForInvoice() {
        $scope.isInvoiceMode = true;
        if ($scope.txnDetailList === undefined) {
            return;
        }
        //$scope.txnDetailGridApi.selection.selectAllRows();
        for (var i = 0; i < $scope.txnDetailList.data.length; i++) {
            //we can refund only invoiced items.
            if ($scope.txnDetailList.data[i].txdeQtyBalance == 0) {
                continue;
            }
            $scope.txnDetailGridApi.selection.selectRow($scope.txnDetailList.data[i]);
            $scope.txnDetailList.data[i].invoiced = true;
            $scope.txnDetailList.data[i].txdeQtyInvoiced = $scope.txnDetailList.data[i].txdeQtyBalance;
            //evaluatRowItem($scope.txnDetailList.data[i]);
        }
        //totalTransaction();
        $scope.allItemsSelected=true;
        changeToInvoiceMode();
    };
    function unSelectAllRows() {
        $scope.isInvoiceMode = false;
        if ($scope.txnDetailList === undefined) {
            return;
        }
        $scope.txnDetailGridApi.selection.clearSelectedRows();
        for (var i = 0; i < $scope.txnDetailList.data.length; i++) {
            $scope.txnDetailList.data[i].invoiced = false;
            $scope.txnDetailList.data[i].txdeQtyInvoiced = 0;
            //evaluatRowItem($scope.txnDetailList.data[i]);
        }
        //totalTransaction();
        $scope.allItemsSelected=false;
        changeToInvoiceMode();
    };
    $scope.toggleAllRowSelection = function() {
        if ($scope.allItemsSelected) {
            unSelectAllRows();
        } else {
            selectAllRowsForInvoice();
        }
    };
    function saveAsDraft()
    {
        if ($scope.isViewMode) {
            return;
        }
        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;
        $scope.txnHeaderForm.customer = $scope.model.customer;
        $scope.txnHeaderForm.temporarySaved = true;


        var pageId;
        if ($scope.pageData === undefined) {
            pageId = -1;
        } else {
            pageId = $scope.pageData.id;
        }
        $scope.pageData = multiPageService.addPage(pageId, $scope.txnHeaderForm.txhdTxnType, $scope.txnHeaderForm.txhdTxnNr===undefined?'':$scope.txnHeaderForm.txhdTxnNr,$scope.txnHeaderForm);
        //$state.go('dashboard.openDraftPageList');
    }
    var promise;
    (function refresh() {
        saveAsDraft();
        promise = $timeout(refresh, 5000);
    })();

    $scope.$on('$destroy', function () {
        $timeout.cancel(promise);
        promise = undefined;
    });
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    };

    function checkEmailIsValid() {
        //check
        if ($scope.sendEmail && ($scope.txnHeaderForm.txhdEmailTo == undefined || $scope.txnHeaderForm.txhdEmailTo == '')) {
            baseDataService.displayMessage("info","Email Invalid", "Please enter email address");
            return false ;
        }
        if (!$scope.sendEmail) {
            $scope.txnHeaderForm.txhdEmailTo = '';
        }
        return true;
    };
    function calculateItemBackOrder(txnDetail) {
        if ($scope.txnHeaderForm.txhdTxnType.categoryCode != 'TXN_TYPE_SALE') {
            return 0;
        }
        if (txnDetail.product.stockQty!=undefined &&  txnDetail.product.stockQty> 0) {
            if (txnDetail.txdeQuantitySold <= txnDetail.product.stockQty) {
                return 0;
            } else {
                return txnDetail.txdeQuantitySold - txnDetail.product.stockQty;
            }
        } else {
            return txnDetail.txdeQuantitySold;
        }
    }
    function refreshBackOrderColumn() {
        if ($scope.txnDetailList.data!=undefined) {
            for (var i = 0; i < $scope.txnDetailList.data.length; i++) {
                $scope.txnDetailList.data[i].txdeQtyBackOrder = calculateItemBackOrder($scope.txnDetailList.data[i]);
            }
        }
    }

    $scope.addPaymentEvent = function(keyEvent) {
        if (keyEvent.which != 13) {
            return;
        }
        //check if payment should be applied or not.
        if (!$scope.showAddPaymentButtom()) {
            return;
        }
        $scope.addTxnMedia();
    }

});
