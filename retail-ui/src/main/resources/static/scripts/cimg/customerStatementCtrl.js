/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerStatementCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CUSTOMER_ALL_URI, CUSTOMER_STATEMENTS_URI) {
    $scope.accountDebtRptForm = {};
    $scope.accountDebtRptForm.toDate = new Date();
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'customerId', visible:false, enableCellEdit:false},
            {field:'customerType', displayName:'Terms',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'code', displayName:'ABN', enableCellEdit:false, width:'10%'},
            {field:'companyName', displayName:'Company Name',enableCellEdit:false, width:'35%',
                cellTooltip: function(row,col) {
                    return row.entity.companyName
                }
            },
            {field:'address', enableCellEdit:false , width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.address
                }
            },
            {field:'customerStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    $scope.gridOptions.noUnselect= false;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
    };
    getAllCustomers();
    function getAllCustomers() {
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.getStatementReport = function() {
        if ($scope.gridApi.selection.getSelectedRows().length <= 0) {
            baseDataService.displayMessage('info','info','Please select customer/s');
            return;
        }
        $scope.accountDebtRptForm.customerList = $scope.gridApi.selection.getSelectedRows();
        baseDataService.pdfViewerPostMethod(CUSTOMER_STATEMENTS_URI, $scope.accountDebtRptForm);
    }
});
