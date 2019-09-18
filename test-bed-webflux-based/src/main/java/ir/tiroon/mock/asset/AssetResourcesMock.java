package ir.tiroon.mock.asset;


import ir.tiroon.mock.asset.model.AssetReferenceModel;
import ir.tiroon.mock.asset.model.CreateAssetResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/asset")
@Component
public class AssetResourcesMock {

    @PostMapping(value = "/upload")
    public ResponseEntity<CreateAssetResponseModel> uploadAsset( @RequestParam MultipartFile data,
                                                                 @RequestParam String assetType,
                                                                 @RequestParam String mimeType,
                                                                @RequestParam String extension,
                                                                 @RequestParam(required = false) String assetKey) {
            return ResponseEntity.ok().body(CreateAssetResponseModel.builder()
                    .withAssetId(12321L)
                    .withMessage("File uploaded successfully")
                    .build());
    }

    @GetMapping(value = "/{assetId}/reference")
    public ResponseEntity<AssetReferenceModel> getAssetReference(@PathVariable("assetId") Long assetId) {
        return ResponseEntity.ok(AssetReferenceModel.builder().withAssetId(123L).withUrl("http://tiron.it/sdf.jpg").build());
    }

}