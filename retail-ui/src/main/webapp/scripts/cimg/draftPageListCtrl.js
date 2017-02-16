/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('draftPageListCtrl', function($scope, $state, $timeout,baseDataService, multiPageService, SUCCESS, FAILURE) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'dateCreated', displayName:'Date',enableCellEdit:false, width:'15%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'pageType.description', displayName:'Type', enableCellEdit:false, width:'20%'},
            {field:'pageNo', displayName:'No', enableCellEdit:false, width:'20%'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.openPage(row)"></i></a>&nbsp;&nbsp;<i tooltip="Delete" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.deletePage(row)"></i></a>', width:'10%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
    };
    getAllPages();
    function getAllPages() {
       $scope.gridOptions.data = multiPageService.getPageList();
    }

    $scope.openPage = function(row) {
        var pageData = row.entity;
        //multiPageService.openPage(row.entity);
        baseDataService.setRow(pageData.formData);
        baseDataService.setIsPageNew(false);
        if (pageData.pageType.categoryCode === 'TXN_TYPE_SALE' ||  pageData.pageType.categoryCode === 'TXN_TYPE_QUOTE' || pageData.pageType.categoryCode === 'TXN_TYPE_INVOICE' ) {
            console.log('called open 1' );
            $state.go('dashboard.createSaleTransaction');
        }
        if (pageData.pageType.categoryCode === 'PAGE_TYPE_PURCHASE') {
            $state.go('dashboard.purchaseOrderDetail');
        }

    }
    $scope.deletePage = function(row) {
        var pageData = row.entity;
        //baseDataService.setRow(pageData.formData);
        multiPageService.deletePage(pageData.id);
        getAllPages();
    }
    $scope.deleteAll = function() {
        multiPageService.removePageList();
        getAllPages();
    }

});
