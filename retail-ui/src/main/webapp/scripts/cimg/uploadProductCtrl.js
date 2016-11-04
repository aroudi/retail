/**
 * Created by arash on 16/12/2014.
 */
cimgApp.controller('uploadProductCtrl', function($scope, $state,fileUploadService,singleFileUploadService,baseDataService, SUCCESS, FAILURE, PRODUCT_UPLOAD_CSV_URI) {
    $scope.fileName ='Upload products from csv'
    $scope.fileSet = [
        'Upload products from csv'
    ];
    $scope.onFileSelect = function(files) {

        switch ($scope.fileName){

            case 'Upload products from csv':
                uploadUrl = PRODUCT_UPLOAD_CSV_URI;
                break;
        }
        fileUploadService.uploadFileToUrl(files, uploadUrl).then(function (response) {
            serviceResponse = response.data;
            if (serviceResponse.status == SUCCESS) {
                $state.go($scope.listProduct);
            } else {
                baseDataService.displayMessage('info', 'Uploead Failed', 'Error in uploading file' + serviceResponse.message)
            }
        });
    };
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

});
