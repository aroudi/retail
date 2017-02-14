/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productTxnListCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, productId, txnType, SUCCESS, FAILURE, TXNS_OF_PRODUCT, INVOICE_OF_PRODUCT ) {

    initTxnList();
    getAllProductTxns();
    function initTxnList() {
        $scope.txnList = {
            enableFiltering: true,
            enableRowSelection:true,
            showGridFooter: true,
            showColumnFooter: true,
            enableColumnResizing: true,
            enableSorting:true,
            columnDefs: [
                {field:'prodId', visible:false, enableCellEdit:false},
                {field:'txdeId', visible:false, enableCellEdit:false},
                {field:'txhdId', visible:false, enableCellEdit:false},
                {field:'txhdTradingDate', displayName:'Date',enableCellEdit:false, width:'15%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'txnType', displayName:'Txn Type',enableCellEdit:false, width:'10%'},
                {field:'txhdTxnNr', displayName:'Order No',enableCellEdit:false, width:'10%'},
                {field:'client', displayName:'Client',enableCellEdit:false, width:'20%'},
                {field:'txdeValueGross', displayName:'Sell(ex)',enableCellEdit:false, width:'12%',cellFilter: 'currency'},
                {field:'txdeValueNet', displayName:'Sell(inc)',enableCellEdit:false, width:'13%',cellFilter: 'currency'},
                {field:'txdeQuantitySold', displayName:'Qty',enableCellEdit:false, width:'10%'},
                {field:'txdePriceSold', displayName:'Total',enableCellEdit:false, width:'10%'}
            ]
        }
        $scope.txnList.enableRowSelection = true;
        $scope.txnList.multiSelect = false;

        //
        $scope.txnList.onRegisterApi = function (gridApi) {
            $scope.txnListGridApi = gridApi;
            /*
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                //baseDataService.setRow(row.entity);
            });
            */
        };
    }
    function getAllProductTxns() {
        if (txnType === 'INVOICE') {
            baseDataService.getBaseData(INVOICE_OF_PRODUCT + productId).then(function(response){
                $scope.txnList.data = response.data;
            });
        } else {
            baseDataService.getBaseData(TXNS_OF_PRODUCT + productId).then(function(response){
                $scope.txnList.data = response.data;
            });
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
