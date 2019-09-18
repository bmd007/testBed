package ir.tiroon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping
@Component
public class Resource {

    @Autowired
    Client client;

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.defer(() -> {

            //this client use syncBody
//            client.uploadAsset().subscribe(s -> System.out.println("BMD::1>" + s));

            client.uploadAsset2().subscribe(s -> System.out.println("BMD::2>" + s));

            client.uploadAsset3().subscribe(s -> System.out.println("BMD::3>" + s));

            client.uploadAsset4().subscribe(s -> System.out.println("BMD::4>" + s));

            client.uploadAsset5().subscribe(s -> System.out.println("BMD::5>" + s));

            //Not working check the reson in the comments in /uploadLegacy Api
//            client.uploadAsset6().subscribe(s -> System.out.println("BMD::6>" + s));

            return Mono.just("OK");
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////
/////////////this controller is to imitate the behaviour of upload asset resource in fleetGW
    @PostMapping(value = "/assetUploadGateway", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Mono<String> assetUploadGateway(@RequestBody Mono<MultiValueMap<String, Part>> map) {
        return map.flatMap(parts -> {
            try {
                File f = Files.createTempFile("temp", "news").toFile();

                FilePart fieldPart = (FilePart) parts.getFirst("data");
                return
                        fieldPart.transferTo(f).then(Mono.defer(() -> {
                            MultiValueMap<String, Object> parts2 = new LinkedMultiValueMap<>();

                            parts2.add("data", new HttpEntity<>(new FileSystemResource(f)));

                            String extension = ((FormFieldPart) parts.getFirst("extension")).value();
                            String mimeType = ((FormFieldPart) parts.getFirst("mimeType")).value();
                            String assetType = ((FormFieldPart) parts.getFirst("assetType")).value();
                            parts2.add("extension", extension);
                            parts2.add("mimeType", mimeType);
                            parts2.add("assetType", assetType);
                            return client.uploadAsset7(parts2)
                                    .map(s -> s + " though assetUploadGateway ");
                        }))
                                .doAfterTerminate(f::delete);
            } catch (IOException e) {
                return Mono.error(e);
            }
        });
    }

    //Todo: deploy this app and test-bed-servlet-base into jetty and test it. Trigger by postman
    @PostMapping(value = "/assetUploadGatewayPipely", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Mono<String> assetUploadGatewayPipely(@RequestBody Mono<MultiValueMap<String, Part>> map) {
        return map.flatMap(parts -> client.uploadAsset7(parts).map(s -> s + " though assetUploadGatewayPipely "));
    }
////////////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "/uploadWithFilePartAndFieldPart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadWithFilePartAndFieldPart(@RequestPart FilePart filePart, @RequestPart FormFieldPart
            fieldPart) {
        return Mono.defer(() -> {
            File f = new File("C:\\Users\\mahdi.amini\\FilePart_" + System.nanoTime());
            return filePart.transferTo(f).then(Mono.just(fieldPart.value() + "::" + filePart.filename() + " Is uploaded using 'uploadWithFilePartAndFieldPart' recourse"));
        });
    }

    //Todo : edit to work and check
//    @PostMapping(value = "/uploadWithFilePartAndFieldPart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Mono<String> uploadWithFilePartAndFieldPart(@RequestPart Mono<FilePart> filePart, @RequestPart Mono<FormFieldPart> fieldPart) {
//        return Mono.defer(() -> {
//            File f = new File("C:\\Users\\mahdi.amini\\FilePart_" + System.nanoTime());
//            return filePart.transferTo(f).then(Mono.just(fieldPart.value() + "::" + filePart.filename() + " Is uploaded using 'uploadWithFilePartAndFieldPart' recourse"));
//        });
//    }

    @PostMapping(value = "/uploadWithMonoMap", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadWithMonoMap(@RequestBody Mono<MultiValueMap<String, Part>> parts) {
        return parts.flatMap(map -> {
            //Is creation of file a blocking operation
            File f = new File("C:\\Users\\mahdi.amini\\Mono MultiValueMap_" + System.nanoTime());
            FilePart filePart = (FilePart) map.getFirst("filePart");
            FormFieldPart fieldPart = (FormFieldPart) map.getFirst("fieldPart");
            return filePart.transferTo(f).then(Mono.just(fieldPart.value() + "::" + filePart.filename() + " Is uploaded using 'uploadWithMonoMap' recourse"));
        });
    }

    @PostMapping(value = "/uploadWithMap", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadWithMap(@RequestBody MultiValueMap<String, Part> parts) {
        //Is creation of file a blocking operation
        File f = new File("C:\\Users\\mahdi.amini\\MultiValueMap_" + System.nanoTime());
        FilePart filePart = (FilePart) parts.getFirst("filePart");
        FormFieldPart fieldPart = (FormFieldPart) parts.getFirst("fieldPart");
        return filePart.transferTo(f).then(Mono.just(fieldPart.value() + "::" + filePart.filename() + " Is uploaded using 'uploadWithMap' recourse"));
    }

    @PostMapping(value = "/uploadWithFlux", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<String> uploadWithFlux(@RequestBody Flux<Part> parts) {
        return parts.flatMap(part -> {
            if (part instanceof FilePart) {
                File f = new File("C:\\Users\\mahdi.amini\\Flux Part_" + System.nanoTime());
                FilePart filePart = (FilePart) part;
                return filePart.transferTo(f).then(Mono.just(filePart.filename() + " Is uploaded using 'uploadWithFlux' recourse"));
            } else {
                return Mono.just(((FormFieldPart) part).value());
            }
        });
    }

    @PostMapping(value = "/uploadLegacy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadAsset(@RequestParam MultipartFile filePart, @RequestParam String fieldPart) {
        //In webFlux, @RequestParam is always fetch the input from the URL "?"
        //It seems that in Servlet stack, if the consume data type is MultiPartFromData, @RequestParam reads value from Map of parts(client side-> how to populate parts)
        return Mono.defer(() -> {
            File f = new File("C:\\Users\\mahdi.amini\\FilePart_" + System.nanoTime());
            try {
                filePart.transferTo(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Mono.just(fieldPart + "::" + filePart.getOriginalFilename() + " Is uploaded using 'uploadLegacy' recourse");
        });
    }
}