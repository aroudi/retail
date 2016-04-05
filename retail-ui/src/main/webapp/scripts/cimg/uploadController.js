/**
 * Created by arash on 16/12/2014.
 */
function UploadController($scope, $state, fileUploadService,singleFileUploadService,baseDataService, SUCCESS, FAILURE, UPLOAD_BOQ_URI) {

    $scope.fileName ='Bill Of Quantity'
    $scope.fileSet = [
        'Bill Of Quantity' ,
        'Project Inventory'
    ];
    $scope.onFileSelect = function(files) {

        switch ($scope.fileName){

            case 'Bill Of Quantity':
                uploadUrl = UPLOAD_BOQ_URI;
                break;
            case 'Project Inventory':
                uploadUrl = UPLOAD_PROJECT_INVENTORY_URI;
                break;
        }
        fileUploadService.uploadFileToUrl(files, uploadUrl).then(function (response) {
            serviceResponse = response.data;
            if (serviceResponse.status == SUCCESS) {
                //dashboard.boqDetailPerBoqId
                baseDataService.setRowId(serviceResponse.info);
                $state.go('dashboard.boqDetailPerBoqId');
            } else {
            }
        });
    };
    /*
    $scope.$watch('files', function() {
        alert('in watch')
        fileUploadService.uploadFileToUrl($scope.files, UPLOAD_STOPS_URI).then(function (response) {
            serviceResponse = response.data;
            if (serviceResponse.status == SUCCESS) {
                alert('file uploaded successfully')
            } else {
                alert('not able to upload file: ' + serviceResponse.message);
            }
        });
    });
    */
    /*
    $scope.uploadFile = function(fileName) {
        var file1 = $scope.myFile;
        singleFileUploadService.uploadFileToUrl(file1, UPLOAD_STOPS_URI).then(function (response) {
            serviceResponse = response.data;
            if (serviceResponse.status == SUCCESS) {
            } else {
            }
        });
    };
    */
}
