package senai.fatesg.com.cgponto.mapas;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    //Aqui onde coloca o alcance da localização
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Método pega as coordenadas de acordo com o endereço passsado
        //É necessário trocar a String pela variável que será retornada do banco de dados

        LatLng posicao = pegaCoordenadaEndereco("Senai Fatesg - Rua 227-A - Setor Leste Universitário, Goiânia - GO");
        if (posicao != null){
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
            googleMap.moveCamera(update);
        }
    }

    private LatLng pegaCoordenadaEndereco(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getContext());

            List<Address> resultado = geocoder.getFromLocationName(endereco, 1);
            if (!resultado.isEmpty()){
                LatLng posicao = new LatLng(resultado.get(0).getLatitude(), resultado.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
