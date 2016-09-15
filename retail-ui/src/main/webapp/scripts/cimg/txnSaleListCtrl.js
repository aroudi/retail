/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, TXN_ALL_URI, TXN_GET_URI, TXN_EXPORT_PDF) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'20%'},
            {field:'txhdTxnNr', displayName:'Number',enableCellEdit:false, width:'10%'},
            {field:'txhdState', displayName:'State', enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'txhdTxnType.displayName' , displayName:'Type', enableCellEdit:false, width:'10%'},
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>', width:'10%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };
    getAllTxnSale();
    function getAllTxnSale() {
        baseDataService.getBaseData(TXN_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createSaleTransaction');
        });
    }

    $scope.exportToPdf = function(row) {

        var exportUrl = TXN_EXPORT_PDF + row.entity.id;
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
    }


});
