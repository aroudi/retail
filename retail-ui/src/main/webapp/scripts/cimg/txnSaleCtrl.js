/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, MEDIA_TYPE_ALL_URI, PAYMENT_MEDIA_OF_TYPE_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'product.prodSku', displayName:'SKU',enableCellEdit:false, width:'10%'},
            {field:'product.prodName', displayName:'Description',enableCellEdit:false, width:'10%'},
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

    initPageData();
    function initPageData() {

        baseDataService.getBaseData(MEDIA_TYPE_ALL_URI).then(function(response){
            $scope.mediaTypeSet = response.data;
            $scope.mediaType = baseDataService.populateSelectList($scope.mediaType,$scope.mediaTypeSet);
            $scope.onMediaTypeChange();
        });
    }
    $scope.onMediaTypeChange = function () {
        var paymentMediaOfTypeUri = PAYMENT_MEDIA_OF_TYPE_URI + $scope.mediaType.id;
        baseDataService.getBaseData(paymentMediaOfTypeUri).then(function(response){
            $scope.paymentMediaSet = response.data;
            $scope.paymentMedia = baseDataService.populateSelectList($scope.paymentMedia,$scope.paymentMediaSet);
        });
    }


    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default'
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.customer = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

});
