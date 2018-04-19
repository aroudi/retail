/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productCtrl', function($scope, $state, UserService, baseDataService, ngDialog,viewMode, SUCCESS, FAILURE, PRODUCT_ADD_URI, PRODUCT_STATUS_URI, PRODUCT_TYPE_URI, UNOM_ALL_URI, TAXRULE_ALL_URI, taxCodeSet, TAXLEGVARIANCE_ALL_URI, SUPPLIER_ALL_URI, PRODUCT_AUDIT_TRAIL_URI, PRGP_GET_ALL_TREEVIEW_URI) {
    //set default data on the page
    $scope.taxLegVarianceSet = taxCodeSet.data;
    initPageData();
    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }
    function initPageData() {
        $scope.category1Label = 'Category1';
        $scope.category2Label = 'Category2';
        $scope.category3Label = 'Category3';
        if ( baseDataService.getIsPageNew()) {
            $scope.isNewPage = true;
            $scope.productForm = {};
            $scope.productSupplier = {};
            $scope.productForm.prodId = -1;
            $scope.productForm.prouId = -1;
        } else {
            $scope.isNewPage = false;
            $scope.productForm = angular.copy(baseDataService.getRow());
            //fetch supplier info and display on page
            if ($scope.productForm.suppProdPrices != undefined && $scope.productForm.suppProdPrices.length > 0) {
                $scope.productSupplier = $scope.productForm.suppProdPrices[0];
            }
            //baseDataService.setRow({});
            //baseDataService.setIsPageNew(true);
        }
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
            $scope.productSupplier.unitOfMeasure = baseDataService.populateSelectList($scope.productSupplier.unitOfMeasure,$scope.unitOfMeasureSet);
        });

        baseDataService.getBaseData(TAXLEGVARIANCE_ALL_URI).then(function(response){
            $scope.taxLegVarianceSet = response.data;
            $scope.productSupplier.taxLegVariance = baseDataService.populateSelectList($scope.productSupplier.taxLegVariance,$scope.taxLegVarianceSet);
        });
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            if ($scope.supplierSet.length > 0) {
                var supplier = {
                    "id" : -1,
                    "supplierName" : "Select"
                }
                $scope.supplierSet.unshift(supplier);
            }
            $scope.productSupplier.supplier = baseDataService.populateSelectList($scope.productSupplier.supplier,$scope.supplierSet);
            //$scope.changeSupplier();
        });
        baseDataService.getBaseData(PRGP_GET_ALL_TREEVIEW_URI).then(function(response){
            $scope.departmentSet = response.data;
            $scope.productForm.department = baseDataService.populateSelectList($scope.productForm.department,$scope.departmentSet);
            $scope.changeDepartment();
        });

    }
    $scope.changeDepartment = function() {
        populateProductGroups();
    }
    function populateProductGroups() {
        //populate category name
        var newNode = { name: "<<N/A>>", id : -1 };
        if ($scope.productForm.department.children[0] != undefined) {
            $scope.category1Label = $scope.productForm.department.children[0].name;
            $scope.category1Set = $scope.productForm.department.children[0].children;
            $scope.category1Set.unshift(newNode);
            $scope.productForm.category1 = baseDataService.populateSelectList(productForm.category1, $scope.category1Set);
        }
        if ($scope.productForm.department.children[1] != undefined) {
            $scope.category2Label = $scope.productForm.department.children[1].name;
            $scope.category2Set = $scope.productForm.department.children[1].children;
            $scope.category2Set.unshift(newNode);
            $scope.productForm.category2 = baseDataService.populateSelectList(productForm.category2,$scope.category2Set);
        }
        if ($scope.productForm.department.children[2] != undefined) {
            $scope.category3Label = $scope.productForm.department.children[2].name;
            $scope.category3Set = $scope.productForm.department.children[2].children;
            $scope.category3Set.unshift(newNode);
            $scope.productForm.category3 = baseDataService.populateSelectList(productForm.category3,$scope.category3Set);
        }
    }
    //create new product
    $scope.createProduct = function () {
        if ($scope.isNewPage) {
            $scope.productForm.suppProdPrices = [];
            $scope.productSupplier.sprcPrefferBuy = true;
            $scope.productSupplier.id = -1;
            $scope.productSupplier.prodId = -1;
            $scope.productSupplier.deleted = false;
            $scope.productForm.suppProdPrices.push($scope.productSupplier);
        }
        $scope.productForm.costPrice = $scope.productSupplier.price;
        $scope.productForm.prceTaxIncluded = true;
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

    $scope.displayProductPurchaseOrders = function () {
        if ($scope.isNewPage) {
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productPurchaseOrderList.html',
            controller:'productPurchaseOrderListCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productId: function(){return $scope.productForm.prodId}}
        }).then (function (){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.displayProductTxns = function (transactionType) {
        if ($scope.isNewPage) {
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productTxnList.html',
            controller:'productTxnListCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productId: function(){return $scope.productForm.prodId}, txnType: function(){return transactionType}}
        }).then (function (){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.displayProductAuditTrail = function () {
        if ($scope.isNewPage) {
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/generalPopUpList.html',
            controller:'productAuditTrailCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return PRODUCT_AUDIT_TRAIL_URI + $scope.productForm.prodId}}
        }).then (function (selectedItem){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
});
