/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqListCtrl', function($scope, $state, uiGridConstants, purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE, BOQ_GET_ALL_URI, BOQ_GET_URI, CREATE_PO_FROM_BOQ_URI ) {
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'project.projectCode', displayName:'Project No',enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.project.projectCode
                }
            },
            {field:'project.customer.companyName', displayName:'Client',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.project.customer.companyName
                }
            },
            {field:'orderNo', displayName:'Client Order',enableCellEdit:false, width:'10%'},
            {field:'project.projectName',displayName:'Project', enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.project.projectName
                }
            },
            {field:'project.referenceNo', displayName:'Project Ref.',enableCellEdit:false, width:'10%', visible:false,
                cellTooltip: function(row,col) {
                    return row.entity.project.referenceNo
                }
            },
            {field:'dateCreated', displayName:'Created',enableCellEdit:false, width:'8%', cellFilter:'date:\'yyyy-MM-dd HH:mm\'' },
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            //{field:'boqValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            //{field:'boqValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'boqStatus', displayName:'status',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                   return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View Detail" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.viewBoqDetail(row)"></i></a>', width:'5%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            if (row.entity.boqStatus.displayName != 'NEW') {
                //baseDataService.displayMessage('Purchase Order already has been created');
                $scope.gridApi.selection.unSelectRow(row.entity);
            }
            //row.unselectR
            baseDataService.setRow(row.entity);
        });
    };
    getBoqList();
    function getBoqList() {
        baseDataService.getBaseData(BOQ_GET_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.viewBoqDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var boqGetURI = BOQ_GET_URI +  row.entity.id;
        baseDataService.getBaseData(boqGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.viewBoqDetail');
        });
    }
    $scope.generatePurchaseOrder= function() {
        baseDataService.addRow($scope.gridApi.selection.getSelectedRows(), CREATE_PO_FROM_BOQ_URI).then(function(response) {
            var purchaseOrderHeaderList = response.data;
            purchaseOrderService.setSrouceOfData('Auto');
            purchaseOrderService.setGeneratedResultFromBoqList(purchaseOrderHeaderList);
            $state.go('dashboard.purchaseOrderList');
        });
    }

    $scope.isRowSelected = function(){
        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
            return true;
        }
        return false;
    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

});
