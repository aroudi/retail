/**
 * Created by arash on 12/09/2016.
 */
/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('reverseDebtorPaymentCtrl', function($scope, $state, $timeout, $stateParams, baseDataService,uiGridConstants, SUCCESS, FAILURE, CUSTOMER_DEBTOR_PAYMENT_URI, REVERSE_DEBTOR_PAYMENT_URI, CUSTOMER_ALL_URI) {

    initPageData();
    initDebtList();

    function initPageData() {
        $scope.reversalDebtorPaymentForm = {};
        $scope.model={};

        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.customerSet = response.data;
            baseDataService.populateCustomerDropdownList($scope.customerSet);

            $scope.customer = baseDataService.populateSelectList($scope.customer,$scope.customerSet);
            $scope.onCustomerChange();
        });

    }
    function initDebtList() {
        $scope.debtList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter: true,
            enableRowSelection:false,
            expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
            columnDefs: [
                {field: 'txhdTradingDate', displayName:'Date',enableCellEdit:false, width:'15%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'20%'},
                {field: 'id', displayName:'Payment No',enableCellEdit: false, width:'10%'},
                {field: 'txhdValueNett', displayName: 'Paid', enableCellEdit: false, cellFilter: 'currency', width: '20%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum}
            ]
        }
        $scope.debtList.enableRowSelection = true;
        $scope.debtList.multiSelect = false;
        $scope.debtList.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
        };

    }

    function displayLinkedRows(line) {
        line.subGridOptions = {
            enableRowSelection :false,
            enableColumnResizing: true,
            columnDefs :[
                {field: 'tapPaymentDate', displayName:'Date',enableCellEdit:false, width:'20%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:"customerAccountDebt.txhdTxnNr", displayName:'Invoice', width:'20%'},
                {field: 'tapAmountPaid', displayName: 'Paid', enableCellEdit: false, cellFilter: 'currency', width: '20%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum}
            ],
            data:line.lines
        }
    }

    $scope.onCustomerChange = function () {
        //load the customer debt from database
        var getCustomerDebtUrl = CUSTOMER_DEBTOR_PAYMENT_URI + $scope.model.customer.id;
        baseDataService.getBaseData(getCustomerDebtUrl).then(function(response){
            $scope.debtList.data = response.data;
            for (i=0; i<$scope.debtList.data.length; i++) {
                displayLinkedRows($scope.debtList.data[i])
            }

        });
    };

    $scope.reverseDebtorPayment = function () {

        $scope.reversalDebtorPaymentForm.txnDebtorPaymentList = angular.copy($scope.gridApi.selection.getSelectedRows());
        for (i=0; i<$scope.reversalDebtorPaymentForm.txnDebtorPaymentList.length; i++) {
            delete $scope.reversalDebtorPaymentForm.txnDebtorPaymentList[i].subGridOptions;
        }
        $scope.reversalDebtorPaymentForm.customerId = $scope.model.customer.id;
        baseDataService.addRow($scope.reversalDebtorPaymentForm, REVERSE_DEBTOR_PAYMENT_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listSaleTransaction');
            } else {
                alert('Not able to reverse debtor payment. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.validForm = function() {
        if ($scope.model.customer === undefined || $scope.model.customer.id === -1) {
            //baseDataService.displayMessage('info','Form is not valid','Please select supplier');
            return false;
        }
        if ($scope.debtList.data === undefined || $scope.gridApi.selection.getSelectedRows().length < 1) {
            //baseDataService.displayMessage('info','Form is not valid','Product list is empty');
            return false;
        }
        return true;
    }
});
