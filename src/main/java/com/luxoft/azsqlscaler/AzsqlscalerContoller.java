package com.luxoft.azsqlscaler;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.sql.models.SqlDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AzsqlscalerContoller {

    private final TokenCredential tokenCredential;

    @Autowired
    public AzsqlscalerContoller(TokenCredential tokenCredential) {
        this.tokenCredential = tokenCredential;
    }

    // scale azure sql database as per request
    @GetMapping ("/api/scale")
    public String Scale() {
        AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        AzureResourceManager azureResourceManager = AzureResourceManager
                .authenticate(tokenCredential, profile)
                .withDefaultSubscription();
        SqlDatabase database = azureResourceManager
                .sqlServers()
                .getByResourceGroup("scalemysql", "sqldb2scale.database.windows.net")
                .databases()
                .get("db2scale");
        database.update()
                .withTag("MyTag", "SomeValue")
                .apply();
        return "Scaling...";
    }

}
