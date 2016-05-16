/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderListCtrl', function($scope, $state, uiGridConstants,purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE) {
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'10%'},
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'pohCreatedDate', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
            {field:'pohValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohStatus', displayName:'status',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                   return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View Detail" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.viewPohDetail(row)"></i></a>', width:'5%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };

    getPohList();
    function getPohList() {
        $scope.pohSrouceData = purchaseOrderService.getSourceOfData();
        if ($scope.pohSrouceData == 'Auto') {
            purchaseOrderService.setSrouceOfData('');
            $scope.gridOptions.data = purchaseOrderService.getGeneratedResultFromBoqList();
        } else {
        }
    }

    $scope.viewPohDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        if ($scope.pohSrouceData == 'Auto') {
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(row.entity);
            //redirect to the Purchase Order Detail
            $state.go('dashboard.purchaseOrderDetail');
        }
    }
});
