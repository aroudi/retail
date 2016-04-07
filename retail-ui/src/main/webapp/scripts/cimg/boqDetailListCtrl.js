/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, BOQDETAIL_GET_PER_BOQID_URI, PRODUCT_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'billOfQuantity.boqName', displayName:'Header Name',enableCellEdit:false, width:'10%'},
            {field:'billOfQuantity.orderNo', displayName:'Order No',enableCellEdit:false, width:'10%'},
            {field:'billOfQuantity.project.projectName',displayName:'Project', enableCellEdit:false, width:'15%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'5%'},
            {field:'product.prodName', displayName:'Product',enableCellEdit:false, width:'20%'},
            {field:'prodIsNew', displayName:'New',enableCellEdit:false, width:'5%'},
            {field:'quantity', displayName:'Qty',enableCellEdit:false, width:'7%'},
            {field:'cost', displayName:'Cost',enableCellEdit:false, width:'7%'},
            {field:'margin', displayName:'Margin',enableCellEdit:false, width:'8%'},
            {field:'sellPrice', displayName:'Price',enableCellEdit:false, width:'8%'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit Product" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editProduct(row)"></i></a>', width:'5%' }
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
    getBoqDetailPerBoqId();
    function getBoqDetailPerBoqId() {
        var boqDetailUri = BOQDETAIL_GET_PER_BOQID_URI + baseDataService.getRowId();
        baseDataService.getBaseData(boqDetailUri).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editProduct = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var productGetURI = PRODUCT_GET_URI + '/' + row.entity.product.id;
        baseDataService.getBaseData(productGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createProduct');
        });
    }
});
