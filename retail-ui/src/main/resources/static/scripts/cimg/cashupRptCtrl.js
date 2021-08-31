/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('cashupRptCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CASH_SESSION_ALL_RECONCILED_URI, CASH_SESSION_RECONCILIATION_TO_PDF_URI, CASHUP_DETAILED_TXN_RPT_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Report', cellTemplate:'<a href=""><i tooltip="Cashup Summary" tooltip-placement="bottom" class="fa fa-dollar fa-2x" ng-click="grid.appScope.showReconciliationRpt(row)"></i></a> <a href=""><i tooltip="Cashup Detailed Transaction Summary" tooltip-placement="bottom" class="fa fa-bank fa-2x" ng-click="grid.appScope.runCashupDetailedTxnSummaryRpt(row)"></i></a>', width:'20%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'cashSession.id', displayName:'Session Id',enableCellEdit:false, width:'10%'},
            {field:'cashSession.cssnOperator.usrFirstName', displayName:'First Name', enableCellEdit:false, width:'15%'},
            {field:'cashSession.cssnOperator.usrSurName', displayName:'SurName',enableCellEdit:false, width:'20%'},
            {field:'seevEventDate', displayName:'Reconciled on',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'cashSession.cssnStartDate', displayName:'Opened on',enableCellEdit:false, width:'12.5%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'cashSession.cssnActEndDate', displayName:'Ended on',enableCellEdit:false, width:'12.5%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''}
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
        });
    };
    getAllReconciledSessions();
    function getAllReconciledSessions() {
        baseDataService.getBaseData(CASH_SESSION_ALL_RECONCILED_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.showReconciliationRpt = function(row) {
        var exportUrl = CASH_SESSION_RECONCILIATION_TO_PDF_URI + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
        /*
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
        */
    }
    $scope.runCashupDetailedTxnSummaryRpt = function(row) {
        var exportUrl = CASHUP_DETAILED_TXN_RPT_URI + row.entity.cashSession.id;
        baseDataService.pdfViewer(exportUrl);
        /*
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
        */
    }
});
