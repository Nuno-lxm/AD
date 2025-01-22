import requests

def enviar_atualizacao_para_glm(medicamento_codigo, quantidade):

    error = {'code' : 0}

    url = "http://localhost:8000/glm/atualizar-medicamento/"  # Endpoint do GLM
    data = {
        "medicamento_id": medicamento_codigo,
        "quantidade": quantidade
    }
    print(medicamento_codigo, quantidade)
    try:
        response = requests.post(url, json=data)
        print(f"Status Code: {response.status_code}")
        if response.status_code == 200:
            if 'warning' in list(response.json().keys()):
                error['code'] = 2
                error['warning'] = response.json().get('warning')
            print("Medicamento atualizado com sucesso no GLM.")
        else:
            if 'error' in list(response.json().keys()):
                error['code'] = 1
            else:
                error['code'] = 3
                error['erro'] = f"Erro ao atualizar medicamento no GLM: {response.json()}"

    except requests.exceptions.RequestException as e:
        error['erro'] =f"Erro de comunicação com o GLM: {str(e)}"
        error['code'] = 3

    return error