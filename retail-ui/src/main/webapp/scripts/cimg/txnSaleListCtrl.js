/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleListCtrl', function($scope, $state,ngDialog, $timeout,UserService,baseDataService, SUCCESS, FAILURE, TXN_ALL_URI, TXN_GET_URI, TXN_EXPORT_PDF, TXN_TYPE_QUOTE, TXN_TYPE_SALE, TXN_SEARCH_URI, QUOTE_DELETE_URI, TXN_SEARCH_PAGING_URI, CUSTOMER_ALL_URI, TXN_STATUS_ONORDER, SO_STATUS_URI, POH_OF_SO_URI, INVOICE_OF_SO_URI) {

    $scope.saleOrderSearchForm = {};
    $scope.model = {};
    //$scope.model.client={};
    $scope.saleOrderSearchForm.clientId = -1;
    $scope.includeSaleOrder = true;
    $scope.includeQuote = true;
    $scope.backOrderList = []; // add sale order to this for generating purchase order
    $scope.getPage = function(){
        $scope.saleOrderSearchForm.txnTypeList = [];
        if ($scope.includeSaleOrder) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeSale.id);
        }
        if ($scope.includeQuote) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeQuote.id);
        }
        $scope.saleOrderSearchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.saleOrderSearchForm.pageSize = paginationOptions.pageSize;
        if ($scope.model.client != undefined) {
            $scope.saleOrderSearchForm.clientId = $scope.model.client.id;
        } else {
            $scope.saleOrderSearchForm.clientId = -1;
        }
        $scope.saleOrderSearchForm.statusId = $scope.status.id;
        baseDataService.addRow($scope.saleOrderSearchForm, TXN_SEARCH_PAGING_URI).then(function(response) {
            var result = angular.copy(response.data);
            $scope.gridOptions.totalItems = result.totalRecords;
            $scope.gridOptions.data = result.result;
        });
    }

    var paginationOptions = {
        pageNumber:1,
        pageSize:25,
        sort:null
    };


    $scope.gridOptions = {
        paginationPageSizes : [25,50,75,100],
        paginationPageSize:25,
        useExternalPagination: true,
        useExternalSorting:true,
        enableFiltering: true,
        columnDefs: [
            {name:'Action', enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row,false)"></i></a>&nbsp;<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.editTransaction(row,true)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)" ></i></a> </i></a>&nbsp;<a href=""><i tooltip="Delete" ng-show="row.entity.txhdTxnType.categoryCode ===\'TXN_TYPE_QUOTE\'" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeQuote(row)" ></i></a>&nbsp;<a href=""><i tooltip="Linked Purchase Orders" tooltip-placement="bottom" class="fa fa-chain fa-2x" ng-show="row.entity.txhdTxnType.categoryCode !=\'TXN_TYPE_QUOTE\'" ng-click="grid.appScope.getAllPoOfSo(row.entity)" ></i></a> &nbsp;<a href=""><i tooltip="Invoices/Refunds" tooltip-placement="bottom" class="fa fa-newspaper-o fa-2x" ng-show="row.entity.txhdTxnType.categoryCode !=\'TXN_TYPE_QUOTE\'" ng-click="grid.appScope.viewInvoiceList(row.entity)" ></i></a> ', width:'12%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Date',enableCellEdit:false, width:'8%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'15%'},
            {field:'txhdPrjCode', displayName:'Project', enableCellEdit:false, width:'5%'},
            {field:'txhdTxnNr', displayName:'Number',enableCellEdit:false, width:'7%'},
            {field:'txhdTxnType.displayName' , displayName:'Type', enableCellEdit:false, width:'8%'},
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'8%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'7%', cellFilter:'currency'},
            {field:'status', displayName:'Status', enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'createPurchaseOrder', displayName:'Select',enableCellEdit:true, type:'boolean', width:'7%',cellFilter:'booleanFilter', cellTemplate:'<input type="checkbox" ng-model="row.entity.createPurchaseOrder" ng-change="grid.appScope.addSaleOrderToBackOrderList(row.entity)">'}
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
            console.log('newPage =' + newPage + ' pageSize=' + pageSize);
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            $scope.getPage();
        });
    };
    initPageData();
    function initPageData() {
        $scope.saleOrderSearchForm = {};
        baseDataService.getBaseData(TXN_TYPE_SALE).then(function(response){
            $scope.txnTypeSale = response.data;
            baseDataService.getBaseData(TXN_TYPE_QUOTE).then(function(response){
                $scope.txnTypeQuote = response.data;
                $scope.getPage();
            });
        });
        baseDataService.getBaseData(TXN_STATUS_ONORDER).then(function(response){
            $scope.txnStatusOnOrder = response.data;
        });
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.customerSet = response.data;
            baseDataService.populateCustomerDropdownList($scope.customerSet);

            if ($scope.customerSet.length > 0) {
                var customer = {
                    "id" : -1,
                    "companyName" : "All"
                }
                $scope.customerSet.unshift(customer);
            }
            //$scope.client = baseDataService.populateSelectList($scope.client,$scope.customerSet);
        });
        baseDataService.getBaseData(SO_STATUS_URI).then(function(response){
            $scope.statusSet = response.data;
            if ($scope.statusSet.length > 0) {
                var allStatus = {
                    "id" : -1,
                    "displayName" : "All",
                    "description" : "All"
                }
                $scope.statusSet.unshift(allStatus);
            }
            $scope.status = baseDataService.populateSelectList($scope.status,$scope.statusSet);
        });
    }

    $scope.editTransaction = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (viewMode) {
                ngDialog.openConfirm({
                    template:'views/pages/txnSale.html',
                    controller:'txnSaleCtrl',
                    className: 'ngdialog-pdfView',
                    closeByDocument:false,
                    resolve: {viewMode: function(){return true}}
                }).then (function (){
                    }, function(reason) {
                        console.log('Modal promise rejected. Reason:', reason);
                    }
                );
            } else {
                $state.go('dashboard.createSaleTransaction');
            }
        });
    }

    $scope.refundTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            var txnDetailList = response.data.txnDetailFormList;
            for (var i = 0; i < txnDetailList.length; i++) {
                txnDetailList[i].txdeQtyBalance = txnDetailList[i].txdeQtyTotalInvoiced*1 - txnDetailList[i].txdeQtyTotalRefund*1;
            }
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.refundTxn');
        });
    }

    $scope.exportToPdf = function(row) {

        var exportUrl = TXN_EXPORT_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }

    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                $scope.model.client = value;
                $scope.saleOrderSearchForm.clientId = $scope.model.client.id;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.onCustomerChange = function () {
        $scope.saleOrderSearchForm.clientId = $scope.model.client.id;
    }

    $scope.search = function() {
        $scope.saleOrderSearchForm.txnTypeList = [];
        if ($scope.includeSaleOrder) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeSale.id);
        }
        if ($scope.includeQuote) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeQuote.id);
        }
        $scope.saleOrderSearchForm.statusId = $scope.status.id;
        //$scope.saleOrderSearchForm.txnTypeList = angular.copy(txnTypeList);
        baseDataService.addRow($scope.saleOrderSearchForm, TXN_SEARCH_URI).then(function(response){
            $scope.gridOptions.data = response.data;
            populateBackOrderFlagFromBackOrderList();
        });
    }

    $scope.removeQuote = function(row) {

        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete quote??').then(function(result){
            if (result) {
                var deleteUrl = QUOTE_DELETE_URI + row.entity.id;
                baseDataService.getBaseData(deleteUrl).then(function(response){
                    var deleteResponse = response.data;
                    if (deleteResponse.status === SUCCESS) {
                        $scope.gridApi.core.setRowInvisible(row);
                    } else {
                        baseDataService.displayMessage('info','Error!!','Error in deleting quote.');
                    }
                });
            } else {
                return;
            }
        });
    }
    $scope.clearCustomer = function() {
        $scope.model={};
        $scope.model.client ={};
        $scope.model.client.id = -1;
    }

    $scope.addSaleOrderToBackOrderList = function(saleOrder) {
        if ($scope.backOrderList === undefined) {
            $scope.backOrderList = [];
        }
        if (saleOrder.status.categoryCode != "SO_STATUS_OUTSTANDING") {
            baseDataService.displayMessage("info","line is not selectable", "you can only select OUTSTANDING items");
            saleOrder.createPurchaseOrder = false;
            return;
        }
        if (saleOrder.txhdTxnType.categoryCode != "TXN_TYPE_SALE") {
            baseDataService.displayMessage("info","line is not selectable", "you can only select Sale Orders(not Quotes)");
            saleOrder.createPurchaseOrder = false;
            return;
        }
        var itemIndex = baseDataService.getArrIndexOf($scope.backOrderList, saleOrder);
        if (saleOrder.createPurchaseOrder === true) {
            //if item had not been added to the list
            if (itemIndex < 0) {
                $scope.backOrderList.push(saleOrder);
            }

        } else {
            if (itemIndex >= 0) {
                $scope.backOrderList.splice(itemIndex, 1);
            }
        }
    };

    function populateBackOrderFlagFromBackOrderList() {
        if ($scope.backOrderList === undefined || $scope.backOrderList.length < 1) {
            return;
        }
        for (var i = 0; i < $scope.backOrderList.length; i++) {
            if (baseDataService.getArrIndexOf($scope.gridOptions.data, $scope.backOrderList[i]) > 0) {
                $scope.gridOptions.data[i].createPurchaseOrder = true;
            } else {
                $scope.gridOptions.data[i].createPurchaseOrder = false;
            }
        }

    };
    $scope.generatePOFromSO = function() {
        ngDialog.openConfirm({
            template:'views/pages/soToPoReview.html',
            controller:'soToPoReviewCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {saleOrderList: function(){return $scope.backOrderList}}
        }).then (function (soToPoResponse){
                ngDialog.openConfirm({
                    template:'views/pages/soToPoResult.html',
                    controller:'soToPoResultCtrl',
                    className: 'ngdialog-theme-default',
                    closeByDocument:false,
                    resolve: {soToPoResponse: function(){return soToPoResponse}}
                }).then (function (result){
                    for (var i = 0; i < $scope.backOrderList.length; i++) {
                        var arrIndex = baseDataService.getArrIndexOf($scope.gridOptions.data, $scope.backOrderList[i]);
                        if ( arrIndex>= 0) {
                            $scope.gridOptions.data[arrIndex].status = $scope.txnStatusOnOrder;
                            $scope.gridOptions.data[arrIndex].createPurchaseOrder = false;
                        }
                    }
                    $scope.backOrderList = [];
                    console.log('Modal promise rejected. Reason:', reason);
                    }, function(reason) {
                    }
                );

            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    $scope.getAllPoOfSo = function (saleOrder) {
        if (saleOrder == undefined) {
            return;
        }
        //check if this supplier has outstanding purchase order.
        ngDialog.openConfirm({
            template:'views/pages/generalPopUpList.html',
            controller:'purchaseOrderSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return POH_OF_SO_URI + saleOrder.id}}
        }).then (function (selectedItem){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    $scope.viewInvoiceList = function (saleOrder) {
        if (saleOrder == undefined) {
            return;
        }
        //check if this supplier has outstanding purchase order.
        ngDialog.openConfirm({
            template:'views/pages/generalPopUpList.html',
            controller:'invoiceListPopUpCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return INVOICE_OF_SO_URI + saleOrder.id}}
        }).then (function (selectedItem){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    $scope.createSaleOrder = function () {
        //check if customer has access
        if (!UserService.checkUserHasAccess("createSaleTransaction")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have access for creating Sale Order");
            return;
        }
        $state.go('dashboard.createSaleTransaction', {txnType:'txnSale', blankPage:'true'});

    }

    $scope.createQuote = function () {
        //check if customer has access
        if (!UserService.checkUserHasAccess("createSaleTransaction")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have access for creating Quote");
            return;
        }
        $state.go('dashboard.createSaleTransaction', {txnType:'quote', blankPage:'true'});

    }

});
