/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('cashupRptCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CASH_SESSION_ALL_RECONCILED_URI, CASH_SESSION_RECONCILIATION_TO_PDF_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Cashup Summary" tooltip-placement="bottom" class="fa fa-dollar fa-2x" ng-click="grid.appScope.showReconciliationRpt(row)"></i></a>', width:'15%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'cashSession.id', displayName:'Session Id',enableCellEdit:false, width:'10%'},
            {field:'cashSession.cssnOperator.usrFirstName', displayName:'First Name', enableCellEdit:false, width:'15%'},
            {field:'cashSession.cssnOperator.usrSurName', displayName:'SurName',enableCellEdit:false, width:'20%'},
            {field:'seevEventDate', displayName:'Reconciled on',enableCellEdit:false, width:'12.5%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
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
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
    }
});
