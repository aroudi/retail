/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('accountingExportListCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, ACCOUNTING_EXPORT_ALL_URI, ACCOUNTING_EXPORT_GET_CONTENT_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'exportedBy', displayName:'Exported By', enableCellEdit:false, width:'20%'},
            {field:'exportTime', displayName:'Exported On',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'exportSuppliers', displayName:'Suppliers',enableCellEdit:false, cellFilter:'booleanFilter', width:'10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) == true) {
                        return 'green';
                    } else {
                        return 'red';
                    }
                }
            },
            {field:'exportDeliveryNotes', displayName:'Purchases',enableCellEdit:false,cellFilter:'booleanFilter', width:'10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) == true) {
                        return 'green';
                    } else {
                        return 'red';
                    }
                }
            },
            {field:'exportJournalEntries', displayName:'Journal Entry',enableCellEdit:false,cellFilter:'booleanFilter', width:'10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) == true) {
                        return 'green';
                    } else {
                        return 'red';
                    }
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Export" tooltip-placement="bottom" class="fa fa-download fa-2x" ng-click="grid.appScope.export(row)"></i></a>', width:'10%' }
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
        baseDataService.getBaseData(ACCOUNTING_EXPORT_ALL_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }

    $scope.export = function (row) {
        var exportUrl = ACCOUNTING_EXPORT_GET_CONTENT_URI + row.entity.id;
        var hiddenElement = document.createElement('a');
        //$scope.accountingExportForm.cashSessionList
        baseDataService.getStreamData(exportUrl).then(function(response){
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
