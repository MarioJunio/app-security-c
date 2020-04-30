package br.com.security.cliente.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.security.cliente.activities.R;
import br.com.security.cliente.dialog.ImageDialog;
import br.com.security.cliente.model.Checkin;
import br.com.security.cliente.net.WebService;
import br.com.security.cliente.stub.CheckinStatus;
import br.com.security.cliente.wrappers.CheckinType;
import br.com.security.cliente.wrappers.DateType;
import br.com.security.cliente.wrappers.ListItemCheckin;

public class CheckinsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ListItemCheckin> dataSet;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public CheckinsRecyclerAdapter(Context context, List<ListItemCheckin> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        // se for item do tipo Data
        if (viewType == ListItemCheckin.TYPE_DATE)
            viewHolder = new DateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_checkin_date, parent, false));
        else if (viewType == ListItemCheckin.TYPE_CHECKIN_TOP)
            viewHolder = new CheckinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_checkin_top, parent, false));
        else if (viewType == ListItemCheckin.TYPE_CHECKIN_MIDDLE)
            viewHolder = new CheckinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_checkin_mid, parent, false));
        else if (viewType == ListItemCheckin.TYPE_CHECKIN_BOTTOM)
            viewHolder = new CheckinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_checkin_bottom, parent, false));
        else if (viewType == ListItemCheckin.TYPE_CHECKIN_FULL)
            viewHolder = new CheckinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_checkin_full, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // tipo da view
        int type = getItemViewType(position);

        // se for view para exibir a data de agrupamento
        if (type == ListItemCheckin.TYPE_DATE) {
            DateType dataType = (DateType) dataSet.get(position);
            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            dateViewHolder.txGroupDate.setText(dataType.getDate());
        } else {
            final Checkin checkin = ((CheckinType) dataSet.get(position)).getCheckin();

            final CheckinViewHolder checkinViewHolder = (CheckinViewHolder) holder;
            checkinViewHolder.txHorario.setText(dateFormat.format(checkin.getData()));
            checkinViewHolder.txFuncionario.setText(checkin.getFuncionario());
            checkinViewHolder.btnStatus.setText(checkin.getStatus());

            // mostra icone da foto para o usuario clicar e exibir a foto real
            if (checkin.isFoto()) {
                checkinViewHolder.imgFotoInspecao.setVisibility(View.VISIBLE);
            } else {
                checkinViewHolder.imgFotoInspecao.setVisibility(View.GONE);
            }

            // ao clicar no icone da imagem, apresentar foto capturada no checkin
            checkinViewHolder.imgFotoInspecao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkinViewHolder.progressFoto.setVisibility(View.VISIBLE);
                    checkinViewHolder.imgFotoInspecao.setVisibility(View.GONE);

                    WebService.getCheckinFoto(context, checkin.getId(), new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(Bitmap bitmap) {
                            checkinViewHolder.progressFoto.setVisibility(View.GONE);
                            checkinViewHolder.imgFotoInspecao.setVisibility(View.VISIBLE);
                            new ImageDialog(context, bitmap).show();
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkinViewHolder.progressFoto.setVisibility(View.GONE);
                            checkinViewHolder.imgFotoInspecao.setVisibility(View.VISIBLE);
                            Log.v("CheckinsRecyclerAdapter", error.getMessage());
                        }
                    });
                }
            });

            if (checkin.getStatus().equals(CheckinStatus.NORMAL.name()))
                checkinViewHolder.btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            else if (checkin.getStatus().equals(CheckinStatus.SUSPEITO.name()))
                checkinViewHolder.btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_light));
            else if (checkin.getStatus().equals(CheckinStatus.PERIGO.name()))
                checkinViewHolder.btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red_dark));

            checkinViewHolder.txObservacao.setText(checkin.getDescricao());

//            int viewType = getItemViewType(position);

//            if (viewType == ListItemCheckin.TYPE_CHECKIN_BOTTOM || viewType == ListItemCheckin.TYPE_CHECKIN_FULL)
//                checkinViewHolder.lineSeparator.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView txGroupDate;

        public DateViewHolder(View view) {
            super(view);
            this.view = view;
            this.txGroupDate = view.findViewById(R.id.tx_group_date);
        }
    }

    public static class CheckinViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView txFuncionario, txHorario, txObservacao;
        ImageView imgFotoInspecao;
        ProgressBar progressFoto;
        Button btnStatus;

        public CheckinViewHolder(View view) {
            super(view);
            this.view = view;
            this.txFuncionario = view.findViewById(R.id.tx_funcionario);
            this.txHorario = view.findViewById(R.id.tx_horario);
            this.txObservacao = view.findViewById(R.id.tx_observacao);
            this.imgFotoInspecao = view.findViewById(R.id.foto_inspecao);
            this.progressFoto = view.findViewById(R.id.progress_foto);
            this.btnStatus = view.findViewById(R.id.btn_status);
        }
    }

}

