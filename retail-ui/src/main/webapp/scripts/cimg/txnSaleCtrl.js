/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleCtrl', function($scope, $state, $timeout,baseDataService,ngDialog, uiGridConstants, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI) {
    
    
    $scope.txnDetailList = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'product.prodSku', displayName:'SKU', enableCellEdit:false, width:'5%'},
            {field:'product.prodName', displayName:'Name',enableCellEdit:false, width:'30%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size',enableCellEdit:false, width:'5%'},
            {field:'txdeValueLine', displayName:'Cost',enableCellEdit:false, cellFilter:'currency', width:'10%'},
            {field:'txdeValueProfit', displayName:'Profit',enableCellEdit:false, cellFilter:'currency', width:'10%'},
            {field:'txdeValueGross', displayName:'Gross Value',enableCellEdit:false, cellFilter:'currency', width:'10%'},
            {field:'txdeTax', displayName:'Tax',enableCellEdit:false, width:'5%'},
            {field:'txdeValueNet', displayName:'Nett Value',enableCellEdit:false, cellFilter:'currency', width:'10%'},
            {field:'txdeQuantitySold', displayName:'Qty', type:'number', width:'5%'},
            {field:'txdePriceSold', displayName:'Total', cellFilter:'currency', footerCellFilter:'currency',aggregationType: uiGridConstants.aggregationTypes.sum, enableCellEdit:false, width:'10%'}
        ]
    }
    $scope.txnDetailList.enableRowSelection = true;
    $scope.txnDetailList.multiSelect = false;
    $scope.txnDetailList.noUnselect= true;

    //
    $scope.txnDetailList.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };

    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var txnDetail = event.targetScope.row.entity;
        cellData = event.targetScope.row.entity[event.targetScope.col.field];
        //txnDetail.txdeQuantitySold = cellData;
        txnDetail.txdePriceSold =  txnDetail.txdeQuantitySold * txnDetail.txdeValueNet;
    })

    initTxnMediaList();
    initPageData();
    function initPageData() {

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


    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default'
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
            className: 'ngdialog-theme-default'
        }).then (function (value){
                //alert('returned value = ' + value);
                var txnDetail = createTxnDetail();
                txnDetail.product = value;
                txnDetail.unitOfMeasure = txnDetail.product.sellPrice.unitOfMeasure;
                evaluatRowItem(txnDetail)
                $scope.txnDetailList.data.push(txnDetail);
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    
    
    function createTxnDetail () {
        var txnDetailObject = {
            'product' : {},
            'txdePriceOveriden' : false,
            'unitOfMeasure' : {},
            'txdePriceOveriden' : false,
            'txdeLineRefund' : false,
            'txdeItemVoid' : false
        }
        return txnDetailObject;
    }
    function evaluatRowItem (txnDetail) {
        txnDetail.txdeTax = calculateTaxRate(txnDetail.product);
        txnDetail.txdeValueLine = txnDetail.product.sellPrice.prcePrice;
        txnDetail.txdeProfitMargin = getProfitMargin();
        txnDetail.txdeValueProfit =  txnDetail.txdeValueLine*txnDetail.txdeProfitMargin;
        txnDetail.txdeValueGross =  txnDetail.txdeValueProfit + txnDetail.txdeValueLine;
        txnDetail.txdeValueNet =  (txnDetail.txdeValueGross * txnDetail.txdeTax) + txnDetail.txdeValueGross;
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
            taxValue = taxValue + taxRules[i].taxLegVariance.txlvRate;
        }
        return taxValue;
    }

    function getProfitMargin () {
        if ($scope.customer == undefined || $scope.customer==null) {
            return 0.10;
        }
        return $scope.customer.grade.rate;
    }

    function initTxnMediaList() {
        $scope.txnMediaList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter:true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'paymentMedia.paymName',displayName:'Payment Media', visible:true, enableCellEdit:false},
                {field:'txmdAmountLocal', displayName:'Amount', visible:true, cellFilter:'currency', footerCellFilter:'currency', aggregationType: uiGridConstants.aggregationTypes.sum,  enableCellEdit:false}
            ]
        }
        $scope.txnMediaList.enableRowSelection = false;
        $scope.txnMediaList.multiSelect = false;
        $scope.txnMediaList.noUnselect= true;

        //
        $scope.txnMediaList.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                $scope.selectedTxnMediaRow = row.entity;
            });
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
        };

        if (!baseDataService.getIsPageNew()) {
            $scope.txnMediaList.data = $scope.txnHeaderForm.txnMediaFormList;
        }
    }

    $scope.addTxnMedia= function() {
        var rowId;
        if ($scope.txnMediaList.data == undefined && $scope.txnMediaList.data ==null) {
            rowId = 0;
        }
        var rowId = $scope.txnMediaList.data.length + 1000;  //in case of having record, don't mixed up with existing recoreds.
        txnMedia = {
            "id" : rowId,
            "paymentMedia":$scope.paymentMedia,
            "txmdAmountLocal" : $scope.paymentAmount
        }
        $scope.txnMediaList.data.push(txnMedia);
    };
    $scope.removeTxnMedia= function () {
        var selectedRow = $scope.selectedTxnMediaRow;
        rowIndex = getArrIndexOf($scope.txnMediaList.data, selectedRow);
        if (rowIndex>-1) {
            $scope.txnMediaList.data.splice(rowIndex,1);
        }


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

        /*
         var userId = UserService.getUserId();
         if (userId == undefined || userId == 0) {
         alert('you need to login first');
         $state.go('dashboard.login');
         }
         */

        $scope.txnHeaderForm.txnDetailFormList = $scope.txnDetailList.data;
        $scope.txnHeaderForm.txnMediaFormList = $scope.txnMediaList.data;
        $scope.txnHeaderForm.customer = $scope.customer;
        var rowObject = $scope.productForm;
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

});
