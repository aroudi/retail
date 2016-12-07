/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('accountingExportCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, ACCOUNTING_EXPORT_INIT_FORM_URI, ACCOUNTING_EXPORT_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'cssnOperator.usrFirstName', displayName:'First Name', enableCellEdit:false, width:'15%'},
            {field:'cssnOperator.usrSurName', displayName:'SurName',enableCellEdit:false, width:'20%'},
            {field:'cssnStartDate', displayName:'Created on',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
            {field:'cssnStatus', displayName:'Status',enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'accExported', displayName:'Comments',enableCellEdit:false, cellFilter:'accImportFilter', width:'20%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) == true) {
                        return 'green';
                    } else {
                        return 'red';
                    }
                }
            }
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
    //set default data on the page
    initPageData();
    function initPageData() {
        baseDataService.getBaseData(ACCOUNTING_EXPORT_INIT_FORM_URI).then(function(response){
            $scope.accountingExportForm = response.data;
            $scope.gridOptions.data = response.data.cashSessionList;
        });
    }

    //add float
    $scope.export = function () {
        var hiddenElement = document.createElement('a');
        //$scope.accountingExportForm.cashSessionList
        baseDataService.getStreamDataByPost($scope.accountingExportForm,ACCOUNTING_EXPORT_URI).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/octet-stream'});
            //saveAs (blob , outPutFile);
            hiddenElement.href = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            //hiddenElement.target ='_blank';
            hiddenElement.download = 'Retail.txt';
            hiddenElement.click();
        });
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
});
