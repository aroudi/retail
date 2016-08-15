/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productCtrl', function($scope, $state, UserService, baseDataService, ngDialog, SUCCESS, FAILURE, PRODUCT_ADD_URI, PRODUCT_STATUS_URI, PRODUCT_TYPE_URI, UNOM_ALL_URI, TAXRULE_ALL_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.productForm = {};
            $scope.productForm.prodId = -1;
            $scope.productForm.prouId = -1;
        } else {
            $scope.productForm = angular.copy(baseDataService.getRow());
            baseDataService.setRow({});
        }
        initSupplierPriceGrid();
        baseDataService.setIsPageNew(true);
        baseDataService.getBaseData(PRODUCT_STATUS_URI).then(function(response){
            $scope.productStatusSet = response.data;
            $scope.productForm.status = baseDataService.populateSelectList($scope.productForm.status,$scope.productStatusSet);
        });
        baseDataService.getBaseData(PRODUCT_TYPE_URI).then(function(response){
            $scope.productTypeSet = response.data;
            $scope.productForm.prodType = baseDataService.populateSelectList($scope.productForm.prodType,$scope.productTypeSet);
        });
        baseDataService.getBaseData(TAXRULE_ALL_URI).then(function(response){
            $scope.taxRuleSet = response.data;
            $scope.productForm.taxRules = baseDataService.populateMultiSelectList($scope.productForm.taxRules,$scope.taxRuleSet);
        });
        baseDataService.getBaseData(UNOM_ALL_URI).then(function(response){
            $scope.unitOfMeasureSet = response.data;
            //$scope.suppUnitOfMeasure = baseDataService.populateSelectList($scope.suppUnitOfMeasure,$scope.unitOfMeasureSet);
            $scope.productForm.prceUnitOfMeasure = baseDataService.populateSelectList($scope.productForm.prceUnitOfMeasure,$scope.unitOfMeasureSet);
        });
    }

    function initSupplierPriceGrid() {
        $scope.gridOptions = {
            enableFiltering: true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'solId', visible:false, enableCellEdit:false},
                {field:'prodId', visible:false, enableCellEdit:false},
                {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'20%',
                    cellTooltip: function(row,col) {
                        return row.entity.supplier.supplierName
                    }
                },
                {field:'catalogueNo', displayName:'CatNo',enableCellEdit:false, width:'20%',
                    cellTooltip: function(row,col) {
                        return row.entity.catalogueNo
                    }
                },
                {field:'partNo', enableCellEdit:false, width:'15%',
                    cellTooltip: function(row,col) {
                        return row.entity.partNo
                    }
                },
                {field:'unitOfMeasure.unomCode', displayName:'Size',enableCellEdit:false,width:'8%'},
                {field:'unomQty',displayName:'Qty', enableCellEdit:true, type: 'number', width:'7%'},
                {field:'price', enableCellEdit:true, cellFilter: 'currency', width:'10%'},
                {field:'bulkQty', enableCellEdit:true, type: 'number', width:'7%'},
                {field:'bulkPrice', enableCellEdit:true, cellFilter: 'currency', width:'8%'},
                {name:'Action',enableCellEdit:false,sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-show="row.entity.id < 0" ng-click="grid.appScope.removeSuppProdPrice(row)"></i></a>', width:'5%' }

            ]
        }
        $scope.gridOptions.enableRowSelection = false;
        $scope.gridOptions.multiSelect = false;
        $scope.gridOptions.noUnselect= true;

        //
        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
        };

        if (!baseDataService.getIsPageNew()) {
            $scope.gridOptions.data = $scope.productForm.suppProdPrices;
        }
    }
    $scope.addSuppProdPrice = function () {
        ngDialog.openConfirm({
            template:'views/pages/productSupplier.html',
            controller:'addProductSupplierCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productSupplierObject: function(){return {}}}
        }).then (function (updatedProductSupplier){
                if (updatedProductSupplier != undefined) {
                    $scope.addSuppProdPriceToGrid(updatedProductSupplier);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }


    $scope.addSuppProdPriceToGrid= function(productSupplier) {
        if (productSupplier.supplier == undefined || productSupplier.supplier == null) {
            alert('undefined');
            return
        }
        if (checkIfSuppProdExists($scope.gridOptions.data,productSupplier.supplier,productSupplier.suppCatalogueNo) > -1) {
            return;
        }
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        suppProdPrice = {
            "id" : rowId,
            "solId" : -1,
            "prodId" : -1,
            "supplier":productSupplier.supplier,
            "catalogueNo" : productSupplier.suppCatalogueNo,
            "partNo" : productSupplier.suppPartNo,
            "unitOfMeasure" : productSupplier.suppUnitOfMeasure,
            "unomQty" : productSupplier.suppUnomQty,
            "price" : productSupplier.suppPrice,
            "bulkQty" : productSupplier.suppBulkQty,
            "bulkPrice" : productSupplier.suppBulkPrice
        }
        $scope.gridOptions.data.push(suppProdPrice);
    };

    $scope.removeSuppProdPrice = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            if (result) {
                row.entity.deleted = true;
                $scope.gridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
    }

    function checkIfSuppProdExists(arr,supplier, catalogNo) {
        if (arr == undefined || supplier== undefined || catalogNo==undefined)
            return -1;
        for (var j = 0; j < arr.length; j++) {
            if ( (arr[j].supplier.supplierCode === supplier.supplierCode)&&(arr[j].catalogueNo === catalogNo)) {
                return j;
            }
        }
        return -1;
    };


    //create new product
    $scope.createProduct = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        $scope.productForm.suppProdPrices = $scope.gridOptions.data;
        var rowObject = $scope.productForm;
        baseDataService.addRow(rowObject, PRODUCT_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listProduct');
            } else {
                alert('Not able to save Product. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }
});
