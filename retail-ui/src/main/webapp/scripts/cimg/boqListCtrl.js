/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqListCtrl', function($scope, $state, uiGridConstants, purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE, BOQ_GET_ALL_URI, BOQ_GET_URI, CREATE_PO_FROM_BOQ_URI, BOQ_SEARCH_PAGING_URI, CUSTOMER_ALL_URI, PROJECT_GET_ALL_URI, BOQ_STATUS_URI, BOQ_EXPORT_PICKING_SLIP_PDF) {
    $scope.getPage = function(){
        $scope.searchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.searchForm.pageSize = paginationOptions.pageSize;
        if ($scope.project != undefined) {
            $scope.searchForm.projectId = $scope.project.id;
        } else {
            $scope.searchForm.projectId = -1;
        }
        if ($scope.client != undefined) {
            $scope.searchForm.clientId = $scope.client.id;
        } else {
            $scope.searchForm.clientId = -1;
        }
        if ($scope.status != undefined) {
            $scope.searchForm.statusId = $scope.status.id;
        } else {
            $scope.searchForm.statusId = -1;
        }
        baseDataService.addRow($scope.searchForm, BOQ_SEARCH_PAGING_URI).then(function(response) {
            var result = angular.copy(response.data);
            $scope.gridOptions.totalItems = result.totalRecords;
            $scope.gridOptions.data = result.result;
        });
    }

    var paginationOptions = {
        pageNumber:1,
        pageSize:25,
        sort:null
    };

    $scope.gridOptions = {
        paginationPageSizes : [25,50,75,100],
        paginationPageSize:25,
        useExternalPagination: true,
        useExternalSorting:true,
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View Detail" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.viewBoqDetail(row)"></i></a> &nbsp; <a href=""><i tooltip="Picking Slip" tooltip-placement="bottom" class="fa fa-file-pdf-o fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>', width:'10%' },
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
            {field:'project.projectName',displayName:'Project', enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.project.projectName
                }
            },
            {field:'project.referenceNo', displayName:'Project Ref.',enableCellEdit:false, width:'10%', visible:false,
                cellTooltip: function(row,col) {
                    return row.entity.project.referenceNo
                }
            },
            {field:'dateCreated', displayName:'Created',enableCellEdit:false, width:'8%', cellFilter:'date:\'dd/MM/yyyy HH:mm\'' },
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            //{field:'boqValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            //{field:'boqValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'boqStatus', displayName:'status',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                   return grid.getCellValue(row, col).color
                }
            }
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
        $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            if (sortColumns.length == 0) {
                paginationOptions.sort = null;
            } else {
                paginationOptions.sort = sortColumns[0].sort.direction;
            }
            $scope.getPage();
        });
        gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
            console.log('newPage =' + newPage + ' pageSize=' + pageSize);
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            $scope.getPage();
        });
    };
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

    initPageData();
    function initPageData() {
        $scope.searchForm = {};
        $scope.getPage();
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.clientSet = response.data;
            if ($scope.clientSet.length > 0) {
                var client = {
                    "id" : -1,
                    "companyName" : "All"
                }
                $scope.clientSet.unshift(client);
            }
            $scope.client = baseDataService.populateSelectList($scope.client,$scope.clientSet);
        });
        baseDataService.getBaseData(PROJECT_GET_ALL_URI).then(function(response){
            $scope.projectSet = response.data;
            if ($scope.projectSet.length > 0) {
                var project = {
                    "id" : -1,
                    "projectName" : "All"
                }
                $scope.projectSet.unshift(project);
            }
            $scope.project = baseDataService.populateSelectList($scope.project,$scope.projectSet);
        });
        baseDataService.getBaseData(BOQ_STATUS_URI).then(function(response){
            $scope.statusSet = response.data;
            if ($scope.statusSet.length > 0) {
                var status = {
                    "id" : -1,
                    "displayName" : "All",
                    "description" : "All"
                }
                $scope.statusSet.unshift(status);
            }
            $scope.status = baseDataService.populateSelectList($scope.status,$scope.statusSet);
        });
    }
    $scope.exportToPdf = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var exportUrl = BOQ_EXPORT_PICKING_SLIP_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }

});
