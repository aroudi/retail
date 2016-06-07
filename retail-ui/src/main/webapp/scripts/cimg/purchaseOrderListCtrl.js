/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderListCtrl', function($scope, $state, uiGridConstants,purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE, POH_GET_ALL_URI, POH_GET_URI, DEL_NOTE_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
        expandableRowScope:{
            viewDelNoteDetail : function(row) {
                if (row == undefined || row.entity == undefined) {
                    return;
                }
                var deliveryNoteGetURI = DEL_NOTE_GET_URI +  row.entity.delnId;
                baseDataService.getBaseData(deliveryNoteGetURI).then(function(response){
                    baseDataService.setIsPageNew(false);
                    baseDataService.setRow(response.data);
                    //redirect to the supplier page.
                    $state.go('dashboard.deliveryNote');
                });
            },

            getLinkedDelNote : function (row) {
                if (row.entity.delnNoteNumber ==undefined || row.entity.delnNoteNumber ==null ) {
                    return '***';
                }
                return row.entity.delnNoteNumber;
            },
            subGridVariable: 'subGridScopeVariable'
        } ,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'15%'},
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'40%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'pohCreatedDate', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
            {field:'pohValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
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
            baseDataService.getBaseData(POH_GET_ALL_URI).then(function(response){
                var data = angular.copy(response.data);
                $scope.gridOptions.data = data;
                for (i=0; i<$scope.gridOptions.data.length; i++) {
                    displayLinkedDelNotes($scope.gridOptions.data[i])
                }
            });
        }
    }

    $scope.viewPohDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var pohGetURI = POH_GET_URI +  row.entity.id;
        baseDataService.getBaseData(pohGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.purchaseOrderDetail');
        });
    }

    function displayLinkedDelNotes(line) {
        line.subGridOptions = {
            enableRowSelection :false,
            enableColumnResizing: true,
            columnDefs :[
                {field:"id", visible:false},
                {field:"pohId", visible:false},
                {field:"delnId", visible:false},
                {field:"delnNoteNumber", displayName:"Delivery Note No.", visible:true, width:'40%',
                    cellTemplate:'<a href="" ng-click="grid.appScope.viewDelNoteDetail(row)">{{grid.appScope.getLinkedDelNote(row)}}</a>'
                },
                {field:"delnGrn", displayName:"GRN No", visible:true, width:'40%'},
                {field:"delnDeliveryDate", displayName:"Delivery Date", width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\'' }
            ],
            data:line.linkedDelNotes
        }
    }
});
