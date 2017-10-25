/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('invoiceListPopUpCtrl', function($scope, $state, ngDialog, $timeout, uiGridConstants, baseDataService, searchUrl, SUCCESS, FAILURE, INVOICE_GET_URI ) {
    $scope.title = "Invoice/Refund list";
    $scope.showRefNo = function(row) {
        if (row.txhdOrigTxnNr == undefined || row.txhdOrigTxnNr == null) {
            return '';
        }
        if (row.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
            return 'S.O: ' + row.txhdOrigTxnNr;
        } else {
            return 'Inv: ' + row.txhdOrigTxnNr;
        }
    }
    var txnNumberTemplate = '<div>{{grid.appScope.showRefNo(row.entity)}}</div>';
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:false,
        enableColumnResizing: true,
        showGridFooter:true,
        columnDefs: [
            {name:'Action',enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.viewTransaction(row, true)"></i></a>', width:'10%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'15%'},
            {field:'txhdOrigTxnNr', displayName:'Ref No', cellTemplate: txnNumberTemplate, enableCellEdit:false, width:'10%'},
            {field:'txhdTxnNr', displayName:'Invoice/Refund No',enableCellEdit:false, width:'15%'},
            {field:'invoiceTxnType.displayName', displayName:'Type',enableCellEdit:false, width:'15%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === 'INVOICE') {
                        return 'green';
                    } else if (grid.getCellValue(row, col) === 'REFUND') {
                        return 'red'
                    } else{
                        return 'amber'
                    }
                }
            },
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'}
        ]
    }
    $scope.gridOptions.multiSelect = false;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            //$scope.selectedOption = row.entity;
        });
    };
    searchInvoices();
    function searchInvoices() {
        baseDataService.getBaseData(searchUrl).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
    $scope.viewTransaction = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (viewMode)
            {
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
                if (row.entity.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
                    $state.go('dashboard.createSaleTransaction');
                } else {
                    $state.go('dashboard.refundTxn');
                }
            }
        });
    }
});
