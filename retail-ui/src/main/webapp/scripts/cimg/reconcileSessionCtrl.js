/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('reconcileSessionCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, CASH_SESSION_RECONCILE_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'paymentMedia.paymName', displayName:'Tender', enableCellEdit:false, width:'30%'},
            {field:'mediaValueExpected', displayName:'Expected Value',enableCellEdit:false, width:'15%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'mediaValueActual', displayName:'Actual Value',enableCellEdit:true, width:'15%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'mediaValueDiff', displayName:'Difference',enableCellEdit:false, width:'15%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'sedeComment', displayName:'Comment', enableCellEdit:true, width:'25%'}
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

    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var eventDetail = event.targetScope.row.entity;
        if ( event.targetScope.col.field == 'mediaValueActual') {
            eventDetail['mediaValueDiff'] = eventDetail.mediaValueExpected*1 - eventDetail.mediaValueActual*1 ;
        }

    })



    //set default data on the page
    initPageData();
    function initPageData() {
        $scope.reconciliationForm = angular.copy(baseDataService.getRow());
        //baseDataService.setIsPageNew(true);
        //baseDataService.setRow({});
        $scope.gridOptions.data = $scope.reconciliationForm.sessionEventDetailList;
    }

    //add float
    $scope.reconcileSession = function () {

        $scope.reconciliationForm.sessionEventDetailList = $scope.gridOptions.data;
        var rowObject = $scope.reconciliationForm;
        baseDataService.addRow(rowObject, CASH_SESSION_RECONCILE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listCurrentCashSession');
            } else {
                alert('Not able to save data. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
});
