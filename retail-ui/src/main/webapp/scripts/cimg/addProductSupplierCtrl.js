/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('addProductSupplierCtrl', function($scope, baseDataService,ngDialog, productSupplierObject, SUCCESS, FAILURE, UNOM_ALL_URI, SUPPLIER_ALL_URI, TAXLEGVARIANCE_ALL_URI) {

    populatePageData();
    function populatePageData() {
        $scope.productSupplierBackup = angular.copy(productSupplierObject);
        $scope.productSupplier = productSupplierObject;
        baseDataService.getBaseData(UNOM_ALL_URI).then(function(response){
            $scope.unitOfMeasureSet = response.data;
            $scope.productSupplier.suppUnitOfMeasure = baseDataService.populateSelectList($scope.productSupplier.suppUnitOfMeasure,$scope.unitOfMeasureSet);
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
    }

    $scope.submit = function () {
        if ($scope.productSupplier != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.productSupplier);
        }
    }
    $scope.cancel = function() {
        productSupplierObject = $scope.productSupplierBackup;
        $scope.closeThisDialog('button');
    }
    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.productSupplier.supplier = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.calculatePriceAfterTax = function() {
        console.log('called');
        $scope.productSupplier.suppBulkPrice = $scope.productSupplier.bulkPriceBeforeTax*1 + $scope.productSupplier.taxLegVariance.txlvRate * $scope.productSupplier.bulkPriceBeforeTax;
        $scope.productSupplier.suppPrice = $scope.productSupplier.costBeforeTax*1 + $scope.productSupplier.taxLegVariance.txlvRate * $scope.productSupplier.costBeforeTax
    }
});
