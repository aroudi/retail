/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('accountingExportReportCtrl', function($scope, $state, $timeout, ngDialog, baseDataService, SUCCESS, FAILURE, CASH_SESSION_ACCOUNTING_REPORT_URI, CASH_SESSION_ACCOUNTING_SUMMARY_URI) {

    $scope.searchForm = {};
    $scope.searchForm.searchRange = 'idRange';
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'cssnSessionId',  displayName:'Session',enableFiltering:true, enableCellEdit:false, width:'10%'},
            {field:'jrnAccDesc', displayName:'Memo',enableFiltering:true, enableCellEdit:false, width:'35%'},
            {field:'jrnAccCode', displayName:'Account', enableFiltering:true, enableCellEdit:false, width:'10%'},
            {field:'jrnDebit', displayName:'Debit',enableCellEdit:false, width:'20%', cellFilter: 'currency'},
            {field:'jrnCredit', displayName:'Credit',enableCellEdit:false, width:'20%', cellFilter: 'currency'}
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };
    $scope.accountingSummaryReport = function() {
        var exportUrl = CASH_SESSION_ACCOUNTING_SUMMARY_URI;
        baseDataService.pdfViewerPostMethod(exportUrl, $scope.searchForm);

        /*
        baseDataService.getStreamDataByPost($scope.searchForm, CASH_SESSION_ACCOUNTING_SUMMARY_URI).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
        */

    }
    $scope.search = function() {
        baseDataService.addRow($scope.searchForm, CASH_SESSION_ACCOUNTING_REPORT_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }
});
